package br.com.anteros.iot.actuators.collectors;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.diozero.util.SleepUtil;

import br.com.anteros.client.mqttv3.IMqttDeliveryToken;
import br.com.anteros.client.mqttv3.MqttAsyncClient;
import br.com.anteros.client.mqttv3.MqttCallback;
import br.com.anteros.client.mqttv3.MqttCallbackExtended;
import br.com.anteros.client.mqttv3.MqttException;
import br.com.anteros.client.mqttv3.MqttMessage;
import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Actuator;
import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.things.VehicleEntranceTrigger;
import br.com.anteros.iot.triggers.ShotMoment;

public class VehicleEntranceTriggerCollector extends MqttCollector
		implements Actuator<Boolean>, Runnable, MqttCallback, MqttCallbackExtended {

	private static final Logger LOG = LoggerProvider.getInstance()
			.getLogger(VehicleEntranceTriggerCollector.class.getName());

	private static final String STATUS = "status";
	private static final String OPEN = "open";
	private static final String CLOSE = "close";
	private static final String TOGGLE = "toggle";

	protected Boolean running = false;
	protected Thread thread;
	private MqttAsyncClient mqttClient;

	public VehicleEntranceTriggerCollector(CollectorListener listener, Thing thing) {
		super(listener, thing);
	}

	public VehicleEntranceTriggerCollector() {
	}

	@Override
	public void run() {
		LOG.info("Iniciando coletor do Trigger de entrada de veículos");
		while (running) {
			Thread.yield();
		}

	}

	@Override
	public void startCollect() {
		Assert.notNull(listener);
		this.running = true;
		thread = new Thread(this);
		thread.setName("Trigger de entrada de veículos");
		thread.start();
	}

	@Override
	public void stopCollect() {
		
		if (mqttClient != null) {
			try {
				mqttClient.close();
			} catch (MqttException e) {
				e.printStackTrace();
			}			
		}
		
		this.running = false;

	}

	@Override
	public boolean isRunning() {
		return running ? true : false;
	}

	@Override
	public boolean isSupportedThing(Thing thing) {
		return thing instanceof VehicleEntranceTrigger;
	}

	@Override
	protected Collector setMqttClient(MqttAsyncClient client) {
		this.mqttClient = client;
		return this;
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		try {
			this.mqttClient.subscribe("/" + ((VehicleEntranceTrigger) this.thing).getItemId() + "/data", 1);
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void connectionLost(Throwable cause) {
		try {
			this.mqttClient.reconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		byte[] payload = message.getPayload();
		InputStream stream = new ByteArrayInputStream(payload);

		JsonReader jsonReader = Json.createReader(stream);
		JsonObject receivedPayload = jsonReader.readObject();
		jsonReader.close();

		String value = receivedPayload.getString("status");
		SimpleResult collectResult = new SimpleResult(value);
		thing.setStatus(value);
		listener.onCollect(collectResult, thing);
		
		fireTriggers(ShotMoment.AFTER, thing, collectResult);

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}

	@Override
	public Boolean executeAction(JsonObject receivedPayload, Thing thing) {
		VehicleEntranceTrigger vehicleEntranceTrigger = (VehicleEntranceTrigger) thing;
		String itemId = vehicleEntranceTrigger.getItemId();
		String action = receivedPayload.getString("action");
		MqttMessage messageMQTT = null;
		
		if (STATUS.equalsIgnoreCase(action)) {
			String msg = "{\"action\":\"status\"}";
			messageMQTT = new MqttMessage(msg.getBytes());
		} else if (OPEN.equalsIgnoreCase(action)) {
			String msg = "{\"action\":\"status\"}";
			messageMQTT = new MqttMessage(msg.getBytes());
		} else if (CLOSE.equalsIgnoreCase(action)) {
			String msg = "{\"action\":\"status\"}";
			messageMQTT = new MqttMessage(msg.getBytes());
		} else if (TOGGLE.equalsIgnoreCase(action)) {
			String msg = "{\"action\":\"status\"}";
			messageMQTT = new MqttMessage(msg.getBytes());
		}

		if (messageMQTT != null) {
			messageMQTT.setQos(1);
			try {
				mqttClient.publish("/" + itemId, messageMQTT);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

}
