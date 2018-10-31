package br.com.anteros.iot;

import br.com.anteros.iot.collectors.CollectResult;
import br.com.anteros.iot.triggers.Trigger;
import br.com.anteros.iot.triggers.TriggerType;

public interface Actuator<T> extends Actuable {	
	
	public T executeAction(String action, Thing thing);
	
	default void fireTriggers(TriggerType type, String action, Thing thing, CollectResult result) {
		if (thing.hasTriggers(type, action)) {
		    for (Trigger trigger : thing.getTriggersByType(type, action)) {
		    	trigger.fire(result);
		    }
		}
	}

}
