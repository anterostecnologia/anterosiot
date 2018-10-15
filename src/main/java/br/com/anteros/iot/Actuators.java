package br.com.anteros.iot;

public interface Actuators {
	
	public Collector discoverCollectorToThing(Thing thing);
	
	public Actuator<?> discoverActuatorToThing(Thing thing);

}
