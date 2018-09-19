package br.com.anteros.iot.things.sensors;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class TesteMQTT implements Runnable {

	public void run() {
//		String topic = "Teste";
//		String content = "Enviando mensagem para teste";
//		int qos = 2;
//		String broker = "tcp://10.0.0.152:1883";
//		String clientId = "JavaSample";
//		MemoryPersistence persistence = new MemoryPersistence();
//		
//		MqttCallback callback = new MQTTCallback(clientId);
//
//		try {
//			MqttClient client = new MqttClient(broker, clientId, persistence);
//			MqttConnectOptions connOpts = new MqttConnectOptions();
//			client.setCallback(callback);
//			connOpts.setCleanSession(true);
//			System.out.println("Connecting to broker: " + broker);
//			client.connect(connOpts);
//			System.out.println("Connected");
//			message.setQos(qos);
//			client.publish(topic, message);
//			System.out.println("Message published");
//			client.disconnect();
//			System.out.println("Disconnected");
//			System.exit(0);
//		} catch (MqttException me) {
//			System.out.println("reason " + me.getReasonCode());
//			System.out.println("msg " + me.getMessage());
//			System.out.println("loc " + me.getLocalizedMessage());
//			System.out.println("cause " + me.getCause());
//			System.out.println("excep " + me);
//			me.printStackTrace();
//		}
		
	}	

}
