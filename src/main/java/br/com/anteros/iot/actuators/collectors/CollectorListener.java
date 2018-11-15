package br.com.anteros.iot.actuators.collectors;

import br.com.anteros.iot.Thing;

public interface CollectorListener {
	
	public void onCollect(CollectResult result, Thing thing);

}
