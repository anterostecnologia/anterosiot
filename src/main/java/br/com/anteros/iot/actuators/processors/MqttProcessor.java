package br.com.anteros.iot.actuators.processors;

import org.eclipse.paho.client.mqttv3.MqttClient;

import br.com.anteros.iot.actuators.collectors.CollectResult;

public abstract class MqttProcessor<T extends CollectResult> extends Processor<T>{
	
	protected MqttClient mqttClient;
	
	public MqttProcessor() {
		super();
	}

	public MqttClient getMqttClient() {
		return mqttClient;
	}

	public void setMqttClient(MqttClient mqttClient) {
		this.mqttClient = mqttClient;
	}
}
