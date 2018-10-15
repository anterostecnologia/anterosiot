package br.com.anteros.iot;

public interface Actuator<T> extends Actuable {	
	
	public T executeAction(String action, Thing thing);

}
