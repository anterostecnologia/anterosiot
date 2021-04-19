package br.com.anteros.iot.support;

import java.util.concurrent.ScheduledExecutorService;

import br.com.anteros.client.mqttv3.MqttClient;
import br.com.anteros.client.mqttv3.MqttClientPersistence;
import br.com.anteros.client.mqttv3.MqttConnectOptions;
import br.com.anteros.client.mqttv3.MqttException;
import br.com.anteros.client.mqttv3.MqttSecurityException;

public class AnterosMqttClient extends MqttClient {
	
	private MqttConnectOptions options;

	public AnterosMqttClient(String serverURI, String clientId, MqttClientPersistence persistence,
			ScheduledExecutorService executorService, MqttConnectOptions options) throws MqttException {
		super(serverURI, clientId, persistence, executorService);
		this.options = options;
	}

	public AnterosMqttClient(String serverURI, String clientId, MqttClientPersistence persistence, MqttConnectOptions options)
			throws MqttException {
		super(serverURI, clientId, persistence);
		this.options = options;
	}

	public AnterosMqttClient(String serverURI, String clientId, MqttConnectOptions options) throws MqttException {
		super(serverURI, clientId);
		this.options = options;
	}

	@Override
	public void connect() throws MqttSecurityException, MqttException {
		super.connect(this.options);
	}
	
	public MqttConnectOptions getOptions() {
		return options;
	}

	public void setOptions(MqttConnectOptions options) {
		this.options = options;
	}
	
	

}
