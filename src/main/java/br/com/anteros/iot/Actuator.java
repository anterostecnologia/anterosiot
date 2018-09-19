package br.com.anteros.iot;

public interface Actuator {

	public boolean isSupportedThing(Thing thing);
	
	public boolean executeAction(String action, Thing thing);

}
