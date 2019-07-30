package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.Beacon;

@JsonTypeName(DomainConstants.BEACON)
public class BeaconNode extends ThingNode {

	protected String[] topics;

	public BeaconNode() {
		super();
	}

	public BeaconNode(String itemName, String description, int pin, String[] topics) {
		super(itemName, description);
		this.topics = topics;
	}


	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new Beacon(this);
	}

}
