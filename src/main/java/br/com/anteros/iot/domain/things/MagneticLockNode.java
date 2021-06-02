package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.MagneticLock;

@JsonTypeName(DomainConstants.MAGNETIC_LOCK)
public class MagneticLockNode extends ThingNode {

	private int pin;
	private int timeWaitOpening;

	public MagneticLockNode() {
		super();
	}

	public MagneticLockNode(String itemName, String description, int pin, int timeWaitOpening) {
		super(itemName, description);
		this.pin = pin;
		this.timeWaitOpening = timeWaitOpening;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public int getTimeWaitOpening() {
		return timeWaitOpening;
	}

	public void setTimeWaitOpening(int timeWaitOpening) {
		this.timeWaitOpening = timeWaitOpening;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new MagneticLock(this);
	}
	
	@Override
	public String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException {
		return mapper.writeValueAsString(node);
	}

	@Override
	public String getThingType() {
		return DomainConstants.MAGNETIC_LOCK;
	}


}
