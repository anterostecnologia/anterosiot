package br.com.anteros.iot.support;

import br.com.anteros.client.mqttv3.IMqttDeliveryToken;
import br.com.anteros.client.mqttv3.MqttAsyncClient;
import br.com.anteros.client.mqttv3.MqttCallback;
import br.com.anteros.client.mqttv3.MqttConnectOptions;
import br.com.anteros.client.mqttv3.MqttException;
import br.com.anteros.client.mqttv3.MqttMessage;
import br.com.anteros.client.mqttv3.MqttPersistenceException;
import br.com.anteros.client.mqttv3.MqttSecurityException;
import br.com.anteros.client.mqttv3.persist.MemoryPersistence;

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
		
		if (name.length() > 23) {
			name = name.substring(0, 22);
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
		
		if (name.length() > 23) {
			name = name.substring(0, 22);
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
		connOpts.setMaxInflight(2500);
		connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		connOpts.setKeepAliveInterval(7200);

		client.setCallback(callback);
		client.connect(connOpts);

		return client;
	}
	
	
	public static IMqttDeliveryToken publishError(Exception ex, String deviceName, MqttAsyncClient client) throws MqttPersistenceException, MqttException {
		String payload = "{exception: "+ex.getMessage()+"}";
		MqttMessage message = new MqttMessage(payload.getBytes());
		message.setQos(1);
		return client.publish("/" + deviceName + AnterosIOTService.ERRORS_TOPIC, message);
	}
	
	public static IMqttDeliveryToken publishBoot(String deviceName, MqttAsyncClient client) throws MqttPersistenceException, MqttException {
		String payload = "{boot: true}";
		MqttMessage message = new MqttMessage(payload.getBytes());
		message.setQos(1);
		return client.publish("/" + deviceName + AnterosIOTService.BOOT_TOPIC, message);
	}


	public static IMqttDeliveryToken publishHeartBeat(String deviceName, String deviceType, String status, Boolean controllerRunning, String hostAddress, MqttAsyncClient client) throws MqttPersistenceException, MqttException {
		String payload = "{ \"status\":\"" + status + "\", \"deviceType\":\"" + deviceType + "\", \"deviceName\":\"" + deviceName + "\", \"ip\":\"" + hostAddress + "\"}";
		MqttMessage message = new MqttMessage(payload.getBytes());
		message.setQos(1);
		return client.publish("/" + deviceName + AnterosIOTService.HEARTBEAT_TOPIC, message);
		
	}

}
