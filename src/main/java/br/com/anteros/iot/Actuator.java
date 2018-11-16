package br.com.anteros.iot;

import javax.json.JsonObject;

public interface Actuator<T> extends Actuable {	
	
	public T executeAction(JsonObject receivedPayload, Thing thing);
	
	

}
