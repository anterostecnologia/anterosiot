package br.com.anteros.iot.domain.things.parts;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PartNode;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.things.parts.GreenLEDSemaphorePart;

@JsonTypeName(DomainConstants.GREEN_LED_SEMAPHORE)
public class GreenLEDSemaphorePartNode extends PartNode {	
	protected int pin;

	public GreenLEDSemaphorePartNode() {
		super();
	}

	public GreenLEDSemaphorePartNode(String itemName, String description, int pin) {
		super(itemName, description);
		this.pin = pin;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new GreenLEDSemaphorePart(this);
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}
	
	@Override
	public String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException {
		return mapper.writeValueAsString(node);
	}

	@Override
	public String getThingType() {
		return DomainConstants.GREEN_LED_SEMAPHORE;
	}

}
