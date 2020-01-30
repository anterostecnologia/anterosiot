package br.com.anteros.iot.support;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.diozero.util.SleepUtil;

import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.iot.app.AnterosIOTService;

public class MqttHelper {

	public static MqttAsyncClient createAndConnectMqttClient(String uri, String name, String userName, String password,
			boolean automaticReconnect, boolean cleanSession) throws MqttSecurityException, MqttException {
		MqttAsyncClient client = null;

		if (StringUtils.isBlank(name)) {
			name = MqttAsyncClient.generateClientId();
		}

		MemoryPersistence memoryPersistence = new MemoryPersistence();
		client = new MqttAsyncClient(uri, name, memoryPersistence);
		
		MqttConnectOptions connOpts = new MqttConnectOptions();		

		if (!StringUtils.isBlank(userName) && !StringUtils.isBlank(password)) {
			connOpts.setUserName(userName);
			connOpts.setPassword(password.toCharArray());
		}
		connOpts.setAutomaticReconnect(automaticReconnect);
		connOpts.setCleanSession(cleanSession);	
		connOpts.setConnectionTimeout(20);
		connOpts.setMaxInflight(2000);
		connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		connOpts.setKeepAliveInterval(7200);

		client.connect(connOpts);


		return client;
	}
	
	public static MqttAsyncClient createAndConnectMqttClient(String uri, String name, String userName, String password,
			boolean automaticReconnect, boolean cleanSession, MqttCallback callback) throws MqttSecurityException, MqttException {
		MqttAsyncClient client = null;

		if (StringUtils.isBlank(name)) {
			name = MqttAsyncClient.generateClientId();
		}

		MemoryPersistence memoryPersistence = new MemoryPersistence();
		client = new MqttAsyncClient(uri, name, memoryPersistence);
		
		MqttConnectOptions connOpts = new MqttConnectOptions();
		
		
		

		if (!StringUtils.isBlank(userName) && !StringUtils.isBlank(password)) {
			connOpts.setUserName(userName);
			connOpts.setPassword(password.toCharArray());
		}
		connOpts.setAutomaticReconnect(automaticReconnect);
		connOpts.setCleanSession(cleanSession);	
		connOpts.setConnectionTimeout(20);
		connOpts.setMaxInflight(1000);
		connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		connOpts.setKeepAliveInterval(7200);

		client.setCallback(callback);
		client.connect(connOpts);

		return client;
	}
	
	
	public static void publishError(Exception ex, String deviceName, MqttAsyncClient client) throws MqttPersistenceException, MqttException {
		String payload = "{exception: "+ex.getMessage()+"}";
		MqttMessage message = new MqttMessage(payload.getBytes());
		message.setQos(1);
		client.publish("/" + deviceName + AnterosIOTService.ERRORS_TOPIC, message);
	}
	
	public static void publishBoot(String deviceName, MqttAsyncClient client) throws MqttPersistenceException, MqttException {
		String payload = "{boot: true}";
		MqttMessage message = new MqttMessage(payload.getBytes());
		message.setQos(1);
		client.publish("/" + deviceName + AnterosIOTService.BOOT_TOPIC, message);
	}


	public static void publishHeartBeat(String deviceName, String deviceType, String status, Boolean controllerRunning, String hostAddress, MqttAsyncClient client) throws MqttPersistenceException, MqttException {
		String payload = "{ \"status\":\"" + status + "\", \"deviceType\":\"" + deviceType + "\", \"deviceName\":\"" + deviceName + "\", \"ip\":\"" + hostAddress + "\"}";
		MqttMessage message = new MqttMessage(payload.getBytes());
		message.setQos(1);
		client.publish("/" + deviceName + AnterosIOTService.HEARTBEAT_TOPIC, message);
		
	}

}
