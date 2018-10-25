package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.LampSonoff;

@JsonTypeName(DomainConstants.LAMP)
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

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}
