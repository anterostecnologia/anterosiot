package br.com.anteros.iot.things.sensors;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTCallback implements MqttCallback {
	private String instanceData = "";

	public MQTTCallback(String instance) {
		instanceData = instance;
	}

	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost on instance \"" + instanceData + "\" with cause \"" + cause.getMessage()
				+ "\" Reason code " + ((MqttException) cause).getReasonCode() + "\" Cause \""
				+ ((MqttException) cause).getCause() + "\"");
		cause.printStackTrace();
	}

	

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		try {
			System.out.println("Message arrived: \"" + message.toString() + "\" on topic \"" + topic.toString()
					+ "\" for instance \"" + instanceData + "\"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			System.out.println(
					"Delivery token \"" + token.hashCode() + "\" received by instance \"" + instanceData + "\"");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}