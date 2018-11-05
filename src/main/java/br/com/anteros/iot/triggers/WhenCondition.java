package br.com.anteros.iot.triggers;

import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;

public class WhenCondition {

	public static String ALL_VALUES = "all_values";
	
	private Thing thing;
	private Part part;
	private String actionOrValue;

	public WhenCondition(Thing thing, Part part, String actionOrValue) {
		this.thing = thing;
		this.part = part;
		this.actionOrValue = actionOrValue;
	}
	
	public static WhenCondition of(Thing thing, Part part, String actionOrValue) {
		return new WhenCondition(thing, part, actionOrValue);
	}

	public Thing getThing() {
		return thing;
	}

	public void setThing(Thing thing) {
		this.thing = thing;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public String getActionOrValue() {
		return actionOrValue;
	}

	public void setActionOrValue(String actionOrValue) {
		this.actionOrValue = actionOrValue;
	}

	
}
