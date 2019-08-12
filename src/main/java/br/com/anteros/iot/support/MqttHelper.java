package br.com.anteros.iot.support;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.diozero.util.SleepUtil;

import br.com.anteros.core.utils.StringUtils;

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
		connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		connOpts.setKeepAliveInterval(MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT);

		client.connect(connOpts);
		
		while (!client.isConnected()) {
			SleepUtil.sleepMillis(2000);
		}

		return client;
	}

}
