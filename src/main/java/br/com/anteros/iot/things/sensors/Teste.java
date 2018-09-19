package br.com.anteros.iot.things.sensors;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Teste {

	public static void main(String[] args) {
		String topic = "Teste";
		String content = "Enviando mensagem para teste";
		int qos = 2;
		String broker = "tcp://10.0.0.152:1883";
		String clientId = "JavaSample";
		MemoryPersistence persistence = new MemoryPersistence();
		
		MqttCallback callback = new MQTTCallback(clientId);

		try {
			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			sampleClient.setCallback(callback);
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: " + broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected");
			System.out.println("Enviando mensagem para teste : " + content);
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(qos);
			sampleClient.publish(topic, message);
			sampleClient.subscribe("Teste");
			System.out.println("Message published");
//			sampleClient.disconnect();
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}

}
