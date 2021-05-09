package br.com.anteros.iot.support;

import br.com.anteros.client.mqttv3.MqttCallback;
import br.com.anteros.client.mqttv3.MqttConnectOptions;
import br.com.anteros.client.mqttv3.MqttException;
import br.com.anteros.client.mqttv3.MqttMessage;
import br.com.anteros.client.mqttv3.MqttPersistenceException;
import br.com.anteros.client.mqttv3.MqttSecurityException;
import br.com.anteros.client.mqttv3.persist.MemoryPersistence;
import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.iot.app.AnterosIOTService;

public class MqttHelper {

	public static AnterosMqttClient createMqttClient(String uri, String name, String userName,
			String password, boolean automaticReconnect, boolean cleanSession)
			throws MqttSecurityException, MqttException {
		AnterosMqttClient client = null;

		if (StringUtils.isBlank(name)) {
			name = AnterosMqttClient.generateClientId();
		}

		if (name.length() > 23) {
			name = name.substring(0, 22);
		}

		MqttConnectOptions connOpts = new MqttConnectOptions();

		if (!StringUtils.isBlank(userName) && !StringUtils.isBlank(password)) {
			connOpts.setUserName(userName);
			connOpts.setPassword(password.toCharArray());
		}
		connOpts.setAutomaticReconnect(automaticReconnect);
		connOpts.setCleanSession(cleanSession);
		connOpts.setConnectionTimeout(30);
		connOpts.setMaxInflight(2000);
		connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		connOpts.setKeepAliveInterval(20);

		MemoryPersistence memoryPersistence = new MemoryPersistence();
		client = new AnterosMqttClient(uri, name, memoryPersistence, connOpts);

		return client;
	}

	public static AnterosMqttClient createMqttClient(String uri, String name, String userName,
			String password, boolean automaticReconnect, boolean cleanSession, MqttCallback callback)
			throws MqttSecurityException, MqttException {
		AnterosMqttClient client = null;

		if (StringUtils.isBlank(name)) {
			name = AnterosMqttClient.generateClientId();
		}

		if (name.length() > 23) {
			name = name.substring(0, 22);
		}

		MqttConnectOptions connOpts = new MqttConnectOptions();

		if (!StringUtils.isBlank(userName) && !StringUtils.isBlank(password)) {
			connOpts.setUserName(userName);
			connOpts.setPassword(password.toCharArray());
		}
		connOpts.setAutomaticReconnect(automaticReconnect);
		connOpts.setCleanSession(cleanSession);
		connOpts.setConnectionTimeout(30);
		connOpts.setMaxInflight(2500);
		connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		connOpts.setKeepAliveInterval(20);

		MemoryPersistence memoryPersistence = new MemoryPersistence();
		client = new AnterosMqttClient(uri, name, memoryPersistence, connOpts);
		
		client.setCallback(callback);

		return client;
	}

	public static void publishError(Exception ex, String deviceName, AnterosMqttClient client)
			throws MqttPersistenceException, MqttException {
		String payload = "{exception: " + ex.getMessage() + "}";
		MqttMessage message = new MqttMessage(payload.getBytes());
		message.setQos(1);
		client.publish("/" + deviceName + AnterosIOTService.ERRORS_TOPIC, message);
	}

	public static void publishBoot(String deviceName, AnterosMqttClient client)
			throws MqttPersistenceException, MqttException {
		String payload = "{boot: true}";
		MqttMessage message = new MqttMessage(payload.getBytes());
		message.setQos(1);
		client.publish("/" + deviceName + AnterosIOTService.BOOT_TOPIC, message);
	}

	public static void publishHeartBeat(String deviceName, String deviceType, String status, Boolean controllerRunning,
			String hostAddress, AnterosMqttClient client) throws MqttPersistenceException, MqttException {
		String payload = "{ \"status\":\"" + status + "\", \"deviceType\":\"" + deviceType + "\", \"deviceName\":\""
				+ deviceName + "\", \"ip\":\"" + hostAddress + "\"}";
		MqttMessage message = new MqttMessage(payload.getBytes());
		message.setQos(1);
		client.publish("/" + deviceName + AnterosIOTService.HEARTBEAT_TOPIC, message);

	}

}
