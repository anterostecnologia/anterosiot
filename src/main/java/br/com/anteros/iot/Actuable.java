package br.com.anteros.iot;

import javax.json.JsonObject;

import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.triggers.Trigger;
import br.com.anteros.iot.triggers.ShotMoment;

public interface Actuable {
	
	public boolean isSupportedThing(Thing thing);
	
	default void fireTriggers(ShotMoment type, String action, Thing thing, CollectResult result) {
		if (thing.hasTriggers(type, action)) {
		    for (Trigger trigger : thing.getTriggersByType(type, action)) {
		    	trigger.fire(result, null);
		    }
		}
	}
	
	default void fireTriggers(ShotMoment type, String action, Thing thing, CollectResult result, JsonObject additionalInformation) {
		if (thing.hasTriggers(type, action)) {
		    for (Trigger trigger : thing.getTriggersByType(type, action)) {
		    	trigger.fire(result, additionalInformation);
		    }
		}
	}
	
	default void fireTriggers(ShotMoment type,Thing thing, CollectResult result) {
		String value = result.getValueAsString();
		if (thing.hasTriggers(type, value)) {
		    for (Trigger trigger : thing.getTriggersByType(type, value)) {
		    	trigger.fire(result, null);
		    }
		}
	}
	
	default void fireTriggers(ShotMoment type,Thing thing, CollectResult result, JsonObject additionalInformation) {
		String value = result.getValueAsString();
		if (thing.hasTriggers(type, value)) {
		    for (Trigger trigger : thing.getTriggersByType(type, value)) {
		    	trigger.fire(result, additionalInformation);
		    }
		}
	}

}
