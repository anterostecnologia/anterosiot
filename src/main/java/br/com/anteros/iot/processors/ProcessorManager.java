package br.com.anteros.iot.processors;

import org.eclipse.paho.client.mqttv3.MqttCallback;

public interface ProcessorManager extends MqttCallback, Runnable {

	public void start();

	public void stop();

}
