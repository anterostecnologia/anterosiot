package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.RFIDReader;

@JsonTypeName(DomainConstants.RFID_NODE)
public class RFIDReaderNode extends ThingNode {
	
	protected String[] topics;
	protected RFIDModel model;

	public RFIDReaderNode() {
		super();
	}

	public RFIDReaderNode(String itemName, String description, String[] topics,RFIDModel model) {
		super(itemName, description);
		this.topics = topics;
		this.model = model;
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new RFIDReader(this);
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	public RFIDModel getModel() {
		return model;
	}

	public void setModel(RFIDModel model) {
		this.model = model;
	}
}
