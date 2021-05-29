package br.com.anteros.iot;

import javax.json.JsonObject;

import br.com.anteros.iot.actuators.collectors.CollectorListener;

public interface Actuator<T> extends Actuable {	
	
	public T executeAction(IOTContext context);
	
	

}
