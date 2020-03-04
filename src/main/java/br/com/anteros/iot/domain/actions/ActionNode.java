package br.com.anteros.iot.domain.actions;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.actions.ExecutionCondition;
import br.com.anteros.iot.domain.PartNode;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;

public class ActionNode extends PlantItemNode {

	private ThingNode thing;
	private PartNode part;
	private String action;
	private String message;
	private String[] topics;
	private ExecutionCondition executionCondition;
	
	public ActionNode(ThingNode thing, PartNode part, String action, String message, String[] topics) {
		super();
		this.thing = thing;
		this.part = part;
		this.action = action;
		this.message = message;
		this.topics = topics;
	}
	
	public ActionNode(ThingNode thing, PartNode part, String action) {
		super();
		this.thing = thing;
		this.part = part;
		this.action = action;
	}
	
	public ActionNode() {
		
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return null;
	}

	public ExecutionCondition getExecutionCondition() {
		return executionCondition;
	}

	public void setExecutionCondition(ExecutionCondition executionCondition) {
		this.executionCondition = executionCondition;
	}
	
	
}
