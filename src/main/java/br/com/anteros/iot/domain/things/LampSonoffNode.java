package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.LampSonoff;

@JsonTypeName(DomainConstants.LAMP_SONOFF)
public class LampSonoffNode extends ThingNode {

	private String topic;
	
	public LampSonoffNode() {
		super();
	}

	public LampSonoffNode(String itemName, String description, String topic) {
		super(itemName, description);
		this.topic = topic;
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}


	@Override
	public Thing getInstanceOfThing() {
		return new LampSonoff(this);
	}
	
	@Override
	public String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException {
		return mapper.writeValueAsString(node);
	}

	@Override
	public String getThingType() {
		return DomainConstants.LAMP_SONOFF;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}
