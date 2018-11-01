package br.com.anteros.iot;

import java.util.Set;

public interface Actuators {
	
	public DefaultActuators registerActuators(Set<Class<? extends Actuable>> newActuators);
	
	public Collector discoverCollectorToThing(Thing thing);
	
	public Actuator<?> discoverActuatorToThing(Thing thing);

}
