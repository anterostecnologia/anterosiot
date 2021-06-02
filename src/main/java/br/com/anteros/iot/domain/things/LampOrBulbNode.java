package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
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
	
	@Override
	public String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException {
		return mapper.writeValueAsString(node);
	}

	@Override
	public String getThingType() {
		return DomainConstants.LAMP;
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
