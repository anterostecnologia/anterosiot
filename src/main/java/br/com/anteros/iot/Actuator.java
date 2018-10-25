package br.com.anteros.iot;

import br.com.anteros.iot.triggers.TriggerType;

public interface Actuator<T> extends Actuable {	
	
	public T executeAction(String action, Thing thing);
	
	public void fireTriggers(TriggerType type, String action, Thing thing);

}
