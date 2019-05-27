package br.com.anteros.iot.support;

import java.io.EOFException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import br.com.anteros.core.utils.StringUtils;

public class MqttHelper {

	public static MqttClient createAndConnectMqttClient(String uri, String name, String userName, String password,
			boolean automaticReconnect, boolean cleanSession) throws MqttSecurityException, MqttException {
		MqttClient client = null;

		if (StringUtils.isBlank(name)) {
			name = MqttClient.generateClientId();
		}

		client = new MqttClient(uri, name);
		client.setTimeToWait(2000);
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

		return client;
	}

}
