package br.com.anteros.iot.actuators.collectors;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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

	public static Map<Thing, BlockingQueue<MqttMessage>> mapMqttMessage = new HashMap<>();

	private static final String STATUS = "status";
	private static final String OPEN = "open";
	private static final String CLOSE = "close";
	private static final String TOGGLE = "toggle";

	protected BlockingQueue<MqttMessage> blockingQueue = new LinkedBlockingQueue<MqttMessage>();

	protected Boolean running = false;
	protected Thread thread;
	protected MqttAsyncClient mqttClient;

	private boolean alreadyConnectedOnce = false;

	public VehicleEntranceTriggerCollector(CollectorListener listener, Thing thing) {
		super(listener, thing);
	}

	public VehicleEntranceTriggerCollector() {
	}

	@Override
	public void run() {
		LOG.info("Iniciando coletor do Trigger de entrada de veículos");
		VehicleEntranceTrigger vehicleEntranceTrigger = (VehicleEntranceTrigger) thing;
		String itemId = vehicleEntranceTrigger.getItemId();
		BlockingQueue<MqttMessage> queue = VehicleEntranceTriggerCollector.mapMqttMessage.get(thing);

		while (running) {
			try {
				if (this.mqttClient != null && this.mqttClient.isConnected()) {
					MqttMessage msg = queue.poll(5000, TimeUnit.MILLISECONDS);
					if (msg != null) {
						this.mqttClient.publish("/" + itemId, msg);

						InputStream stream = new ByteArrayInputStream(msg.getPayload());
						JsonObject message = null;
						JsonReader jsonReader = Json.createReader(stream);
						message = jsonReader.readObject();
						jsonReader.close();

						if (message != null && message.containsKey("receivedPayload")) {
							String action = message.getString("action");

							JsonObject receivedPayload = message.getJsonObject("receivedPayload");
							String code = receivedPayload.getString("value");
							JsonObject additionalInformation = null;
							if (receivedPayload.containsKey("additionalInformation")) {
								additionalInformation = receivedPayload.getJsonObject("additionalInformation");
							}

							String event = additionalInformation.getString("event", "");
							String tpToken = additionalInformation.getString("tpToken", "");
							Boolean isAuthorized = true;
							String value = "toggle";

							if ("openGate".equalsIgnoreCase(action)) {
								value = "open";
							} else if ("closeGate".equalsIgnoreCase(action)) {
								value = "close";
								if (additionalInformation.getBoolean("isException", false)) {
									isAuthorized = false;
								}
							}

							SimpleResult simpleResult = new SimpleResult(
									"{ \"source\" : \"" + itemId + "\", \"value\" : \"" + value + "\", \"code\" : \""
											+ code + "\", \"event\" : \"" + event + "\", \"tpToken\" : \"" + tpToken
											+ "\", \"isAuthorized\":\"" + isAuthorized.toString() + "\"}");

							listener.onCollect(simpleResult, thing);
							fireTriggers(ShotMoment.AFTER, thing, simpleResult);
						}

					}
				} else if (this.mqttClient != null && alreadyConnectedOnce) {
					try {
						this.mqttClient.reconnect();
					} catch (MqttException e) {
						if (new Integer(e.getReasonCode()).equals(new Integer(32110))) {
							SleepUtil.sleepMillis(5000);
						} else {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void startCollect() {
		Assert.notNull(listener);
		VehicleEntranceTriggerCollector.mapMqttMessage.put(thing, blockingQueue);
		this.running = true;
		thread = new Thread(this);
		thread.setName("Trigger de entrada de veículos");
		thread.start();
	}

	@Override
	public void stopCollect() {
		VehicleEntranceTriggerCollector.mapMqttMessage.remove(thing);
		if (mqttClient != null) {
			try {
				if (this.mqttClient.isConnected()) {
					this.mqttClient.disconnect();
				}
				SleepUtil.sleepMillis(500);
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
		if (!alreadyConnectedOnce) {
			alreadyConnectedOnce = true;
		}
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

		SimpleResult collectResult = new SimpleResult(receivedPayload.toString());
		thing.setStatus(receivedPayload.getString("status"));
		listener.onCollect(collectResult, thing);

		fireTriggers(ShotMoment.AFTER, thing, collectResult);

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}

	@Override
	public Boolean executeAction(JsonObject receivedPayload, Thing thing) {
		String action = receivedPayload.getString("action");
		MqttMessage messageMQTT = null;
		BlockingQueue<MqttMessage> queue = VehicleEntranceTriggerCollector.mapMqttMessage.get(thing);

		String msg = null;

		if (STATUS.equalsIgnoreCase(action)) {
			msg = "{\"action\":\"status\", \"receivedPayload\":" + receivedPayload.toString() + "}";
		} else if (OPEN.equalsIgnoreCase(action)) {
			msg = "{\"action\":\"openGate\", \"receivedPayload\":" + receivedPayload.toString() + "}";
		} else if (CLOSE.equalsIgnoreCase(action)) {
			msg = "{\"action\":\"closeGate\", \"receivedPayload\":" + receivedPayload.toString() + "}";
		} else if (TOGGLE.equalsIgnoreCase(action)) {
			msg = "{\"action\":\"toggleGate\", \"receivedPayload\":" + receivedPayload.toString() + "}";
		}

		messageMQTT = msg != null ? new MqttMessage(msg.getBytes()) : null;

		if (messageMQTT != null) {
			messageMQTT.setQos(1);
			try {
				queue.put(messageMQTT);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

}
