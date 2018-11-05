package br.com.anteros.iot.domain.triggers;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.PartNode;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;

public class WhenConditionNode extends PlantItemNode {
	
	

	private ThingNode thing;
	private PartNode part;
	private String[] actionOrValue;
	
	public WhenConditionNode(ThingNode thing, PartNode part, String... actionOrValue) {
		super();
		this.thing = thing;
		this.part = part;
		this.actionOrValue = actionOrValue;
	}
	
	
	public WhenConditionNode() {
		
	}

	public ThingNode getThing() {
		return thing;
	}

	public void setThing(ThingNode thing) {
		this.thing = thing;
	}

	public PartNode getPart() {
		return part;
	}

	public void setPart(PartNode part) {
		this.part = part;
	}

	public String[] getActionOrValue() {
		return actionOrValue;
	}

	public void setAction(String... actionOrValue) {
		this.actionOrValue = actionOrValue;
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return null;
	}
	
	
}
