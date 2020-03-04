package br.com.anteros.iot.actuators.collectors;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import br.com.anteros.client.mqttv3.IMqttDeliveryToken;
import br.com.anteros.client.mqttv3.MqttAsyncClient;
import br.com.anteros.client.mqttv3.MqttCallback;
import br.com.anteros.client.mqttv3.MqttCallbackExtended;
import br.com.anteros.client.mqttv3.MqttException;
import br.com.anteros.client.mqttv3.MqttMessage;
import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.things.BarrierSensorReader;
import br.com.anteros.iot.triggers.ShotMoment;

public class BarrierSensorReaderCollector extends MqttCollector implements Runnable, MqttCallback, MqttCallbackExtended {

	private static final Logger LOG = LoggerProvider.getInstance()
			.getLogger(BarrierSensorReaderCollector.class.getName());

	protected Boolean running = false;
	protected Thread thread;
	private MqttAsyncClient mqttClient;

	@Override
	public void run() {
		LOG.info("Iniciando coletor do Leitor de sensor de barreira");
		while (running) {
			Thread.yield();
		}
	}

	@Override
	protected Collector setMqttClient(MqttAsyncClient client) {
		this.mqttClient = client;
		return this;
	}

	@Override
	public void startCollect() {
		Assert.notNull(listener);
		this.running = true;
		thread = new Thread(this);
		thread.setName("Leitor de sensor de barreira");
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
		return thing instanceof BarrierSensorReader;
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		try {
			this.mqttClient.subscribe("/" + ((BarrierSensorReader) this.thing).getItemId() + "/data", 1);
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
		listener.onCollect(collectResult, thing);

		fireTriggers(ShotMoment.AFTER, thing, collectResult);
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}

}