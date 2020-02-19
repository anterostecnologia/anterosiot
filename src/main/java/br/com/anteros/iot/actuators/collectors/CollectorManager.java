package br.com.anteros.iot.actuators.collectors;

import br.com.anteros.client.mqttv3.MqttCallback;

public interface CollectorManager extends MqttCallback, Runnable {
	
	public void start();
	
	public void pause();
	
	public void stop();
	
	public void resume();
	
}
