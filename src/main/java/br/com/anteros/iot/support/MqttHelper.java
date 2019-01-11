package br.com.anteros.iot.support;

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
		MqttConnectOptions connOpts = new MqttConnectOptions();
		
		if (!userName.isEmpty() && !password.isEmpty()) {
			connOpts.setUserName(userName);
			connOpts.setPassword(password.toCharArray());
		}
		connOpts.setAutomaticReconnect(automaticReconnect);
		connOpts.setCleanSession(cleanSession);
		client.connect(connOpts);
		return client;
	}

}
