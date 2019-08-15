package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.EletronicLock;
import br.com.anteros.iot.things.GenericRelay;

@JsonTypeName(DomainConstants.GENERIC_RELAY)
public class GenericRelayNode extends ThingNode {
	
	protected int pin;

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new GenericRelay(this);		
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

}
