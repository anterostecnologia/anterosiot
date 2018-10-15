package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.LampOrBulb;

@JsonTypeName(DomainConstants.LAMP)
public class LampOrBulbNode extends ThingNode {

	private int pin;


	public LampOrBulbNode() {
		super();
	}

	public LampOrBulbNode(String itemName, String description, int pin) {
		super(itemName, description);
		this.pin = pin;
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new LampOrBulb(this);
	}
}
