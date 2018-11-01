package br.com.anteros.iot.collectors;

import java.util.HashSet;
import java.util.Set;

import javax.json.Json;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.support.MqttHelper;
import br.com.anteros.iot.things.Publishable;

public class SimpleCollectorManager implements CollectorManager, CollectorListener {

	private MqttClient mqttClient;
	private Thread thread;
	private Thing[] things;
	private Boolean running = false;
	private Boolean paused = false;
	private Set<Collector> collectorsRunning = new HashSet<>();
	private Actuators actuators;

	protected SimpleCollectorManager(MqttClient mqttClient, Thing[] things, Actuators actuators) {
		this.mqttClient = mqttClient;
		this.thread = new Thread(this);
		this.things = things;
		this.actuators = actuators;
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

	public static SimpleCollectorManager of(MqttClient mqttClient, Thing[] things, Actuators actuators) {
		return new SimpleCollectorManager(mqttClient, things, actuators);
	}

	@Override
	public void connectionLost(Throwable cause) {

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		if (!paused) {

		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {

	}

	@Override
	public void run() {
		running = true;
		boolean first = true;

		for (Thing thing : things) {
			Collector collector = actuators.discoverCollectorToThing(thing);
			if (collector != null) {
				collector.setListener(this);
				
				if (collector instanceof MqttCollector) {
					MqttClient clientCollector = null;
					try {
						clientCollector = MqttHelper.createAndConnectMqttClient(mqttClient.getServerURI(),
								thing.getThingID() + "_collector", "", "", true, true);
					} catch (MqttException e1) {
						e1.printStackTrace();
					}
					
					((MqttCollector) collector).setMqttClient(clientCollector);
				}
				collector.startCollect();
				collectorsRunning.add(collector);
			}
		}

		while (running) {
			if (first) {
				System.out.println("Coletor de dados rodando...");
				first = false;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Parando coletor dados.");
		for (Collector collector : collectorsRunning) {
			collector.stopCollect();
		}
		collectorsRunning.clear();
		this.thread.interrupt();

	}

	@Override
	public void onCollect(CollectResult result, Thing thing) {
		if (!paused) {
			if (thing instanceof Publishable) {
				String[] topics = ((Publishable) thing).getTopicsToPublishValue();

				for (String topic : topics) {
					try {
						System.out.println(topic);
						MqttMessage message = new MqttMessage(result.toJson(Json.createObjectBuilder()).build().toString().getBytes());
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

}
