package br.com.anteros.iot;

import br.com.anteros.iot.things.Publishable;

public interface Sensor extends Thing, Publishable {

	public long getTimeIntervalForCollect();
	
	public SensorCollectionType getCollectionType();
	
	
}
