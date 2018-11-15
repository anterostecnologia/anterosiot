package br.com.anteros.iot.actuators.collectors;

import org.eclipse.paho.client.mqttv3.MqttCallback;

public interface CollectorManager extends MqttCallback, Runnable {
	
	public void start();
	
	public void pause();
	
	public void stop();
	
	public void resume();
	
}
