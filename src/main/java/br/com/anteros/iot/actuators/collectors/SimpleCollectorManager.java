package br.com.anteros.iot.actuators.collectors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.stream.JsonGenerator;

import br.com.anteros.client.mqttv3.IMqttDeliveryToken;
import br.com.anteros.client.mqttv3.MqttCallback;
import br.com.anteros.client.mqttv3.MqttMessage;
import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.support.AnterosMqttClient;
import br.com.anteros.iot.support.MqttHelper;
import br.com.anteros.iot.things.Publishable;
import com.diozero.util.SleepUtil;

public class SimpleCollectorManager implements CollectorManager, CollectorListener {

	private AnterosMqttClient AnterosMqttClient;
	private Thread thread;
	private Thing[] things;
	private Boolean running = false;
	private Boolean paused = false;
	private Device device;
	private Set<Collector> collectorsRunning = new HashSet<>();
	private Actuators actuators;
	protected String username;
	protected String password;
	
	private static final Logger LOG = LoggerProvider.getInstance().getLogger(SimpleCollectorManager.class.getName());

	protected SimpleCollectorManager(AnterosMqttClient clientMqtt, Thing[] things, Actuators actuators, Device device,
			String username, String password) {
		this.AnterosMqttClient = clientMqtt;
		this.thread = new Thread(this);
		thread.setName("Coletor de dados");
		thread.setPriority(Thread.MAX_PRIORITY);
		this.things = things;
		this.actuators = actuators;
		this.device = device;
		this.username = username;
		this.password = password;
	}

	public void stop() {
		if (running) {
			this.running = false;
		}
	}

	public void start() {
		if (!running) {
			this.thread.start();
		}
	}

	public void pause() {
		if (!paused) {
			this.paused = true;
		}
	}

	public void resume() {
		if (paused) {
			this.paused = false;
		}
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	public static SimpleCollectorManager of(AnterosMqttClient clientMqtt, Thing[] things, Actuators actuators, Device device,
			String username, String password) {
		return new SimpleCollectorManager(clientMqtt, things, actuators, device, username, password);
	}

	@Override
	public void run() {
		running = true;
		boolean first = true;
		
		LOG.info("Iniciando gerenciador de coletores de dados...");

		if (device.hasTelemetries()) {
			LOG.info("Adicionando coletores de telemetria...");
			Collector collector = actuators.discoverCollectorToThing(device);
			if (collector != null) {
				
				while(collector.isRunning()){
					collector.stopCollect();
					SleepUtil.sleepMillis(500);
				}
				
				collector.setListener(this);
				collector.startCollect();
				collectorsRunning.add(collector);
			}
		}

		LOG.info("Adicionando coletores das coisas...");
		for (Thing thing : things) {
			Collector collector = actuators.discoverCollectorToThing(thing);
			if (collector != null) {
				collector.setListener(this);
				
				while(collector.isRunning()){
					collector.stopCollect();
					SleepUtil.sleepMillis(500);
				}
				
				if (collector instanceof MqttCollector) {
					AnterosMqttClient clientCollector = null;
					try {
						clientCollector = MqttHelper.createMqttClient(AnterosMqttClient.getServerURI(),
								thing.getThingID().split("-")[0] + "_collector", username, password, true,
								true, (MqttCallback) collector);
						clientCollector.connect();
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					((MqttCollector) collector).setAnterosMqttClient(clientCollector);
				}
				
				
				collector.startCollect();
				collectorsRunning.add(collector);
			}
		}

		while (running) {
			if (first) {
				LOG.info("Coletor de dados rodando...");
				first = false;
			}
			SleepUtil.sleepMillis(200);
		}
		
		LOG.info("Parando coletor dados...");
		for (Collector collector : collectorsRunning) {
			collector.stopCollect();
		}

		try {
			if (AnterosMqttClient.isConnected())
				AnterosMqttClient.disconnect();
		} catch (Exception e) {
			LOG.error("Ocorreu uma falha ao parar o coletor: " + e.getMessage());
			e.printStackTrace();
		}

		collectorsRunning.clear();
		this.thread.interrupt();

	}

	@Override
	public void onCollect(CollectResult result, Thing thing) {
		if (!paused) {

			if (thing instanceof Publishable) {
				String[] topics = ((Publishable) thing).getTopicsToPublishValue(result);

				for (String topic : topics) {
					try {
						Map<String, Boolean> config = new HashMap<>();

						config.put(JsonGenerator.PRETTY_PRINTING, true);

						JsonObjectBuilder builder = Json.createObjectBuilder();
						JsonObject jsonMessage = result.toJson(builder).build();

						MqttMessage message = new MqttMessage(jsonMessage.toString().getBytes());
						message.setQos(1);
						if (AnterosMqttClient.isConnected()) {
							AnterosMqttClient.publish(topic, message);
							if (thing.showLog()) {
								LOG.info("Coletou dados da coisa " + thing.getThingID() + " e enviou para o tópico " + topic+" => Value "+jsonMessage.toString());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		try {
			this.AnterosMqttClient.reconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}

}
