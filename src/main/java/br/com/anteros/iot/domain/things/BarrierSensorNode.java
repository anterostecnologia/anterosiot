package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.sensors.BarrierSensor;

@JsonTypeName(DomainConstants.BARRIER_SENSOR)
public class BarrierSensorNode extends ThingNode {

	protected int pin;
	protected String[] topics;

	public BarrierSensorNode() {
		super();
	}

	public BarrierSensorNode(String itemName, String description, int pin, String[] topics) {
		super(itemName, description);
		this.topics = topics;
		this.pin = pin;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new BarrierSensor(this);
	}

}