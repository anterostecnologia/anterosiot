package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.EletronicLock;

@JsonTypeName(DomainConstants.ELETRONIC_LOCK)
public class EletronicLockNode extends ThingNode {
	
	protected int pin;

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new EletronicLock(this);		
	}

	@Override
	public String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException {
		return mapper.writeValueAsString(node);
	}

	@Override
	public String getThingType() {
		return DomainConstants.ELETRONIC_LOCK;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

}
