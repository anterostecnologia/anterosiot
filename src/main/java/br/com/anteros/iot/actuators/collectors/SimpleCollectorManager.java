package br.com.anteros.iot.actuators.collectors;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import br.com.anteros.client.mqttv3.IMqttDeliveryToken;
import br.com.anteros.client.mqttv3.MqttAsyncClient;
import br.com.anteros.client.mqttv3.MqttCallback;
import br.com.anteros.client.mqttv3.MqttClient;
import br.com.anteros.client.mqttv3.MqttException;
import br.com.anteros.client.mqttv3.MqttMessage;
import br.com.anteros.client.mqttv3.MqttPersistenceException;

import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.support.MqttHelper;
import br.com.anteros.iot.things.Publishable;

public class SimpleCollectorManager implements CollectorManager, CollectorListener {

	private MqttAsyncClient mqttClient;
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

	protected SimpleCollectorManager(MqttAsyncClient clientMqtt, Thing[] things, Actuators actuators, Device device,
			String username, String password) {
		this.mqttClient = clientMqtt;
		this.thread = new Thread(this);
		thread.setName("Coletor de dados");
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

	public static SimpleCollectorManager of(MqttAsyncClient clientMqtt, Thing[] things, Actuators actuators, Device device,
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
					Thread.yield();
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

				if (collector instanceof MqttCollector) {
					MqttAsyncClient clientCollector = null;
					try {
						clientCollector = MqttHelper.createAndConnectMqttClient(mqttClient.getServerURI(),
								thing.getThingID().split("-")[0] + "_collector", username, password, true,
								true, (MqttCallback) collector);
					} catch (MqttException e1) {
						e1.printStackTrace();
					}

					((MqttCollector) collector).setMqttClient(clientCollector);
				}
				
				while(collector.isRunning()){
					collector.stopCollect();
					Thread.yield();
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
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		LOG.info("Parando coletor dados...");
		for (Collector collector : collectorsRunning) {
			collector.stopCollect();
		}

		try {
			if (mqttClient.isConnected())
				mqttClient.disconnect();
		} catch (MqttException e) {
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

//						JsonWriterFactory writerFactory = Json.createWriterFactory(config);

						JsonObjectBuilder builder = Json.createObjectBuilder();
						JsonObject jsonMessage = result.toJson(builder).build();

//						String jsonString="";	
//						try (Writer writer = new StringWriter()) {
//							writerFactory.createWriter(writer).write(builder.build());
//							jsonString = writer.toString();
//						} catch (IOException e) {
//						}

//						MqttMessage message = new MqttMessage(jsonString.getBytes());

						MqttMessage message = new MqttMessage(jsonMessage.toString().getBytes());
						message.setQos(1);
						mqttClient.publish(topic, message);
					} catch (MqttPersistenceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MqttException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
//		LOG.warn("Conexão com o broker MQTT perdida. Causa: " + cause.toString());
		try {
			this.mqttClient.reconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

}
