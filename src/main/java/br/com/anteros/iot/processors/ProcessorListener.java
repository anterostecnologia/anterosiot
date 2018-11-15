package br.com.anteros.iot.processors;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import br.com.anteros.iot.Thing;

public interface ProcessorListener {
	
	public void execute(MqttMessage message, Thing thing);
}
