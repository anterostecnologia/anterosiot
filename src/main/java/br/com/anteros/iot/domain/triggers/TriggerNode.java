package br.com.anteros.iot.domain.triggers;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.actions.ActionNode;
import br.com.anteros.iot.triggers.TriggerType;

public class TriggerNode extends PlantItemNode {
	
	private String name;
	private TriggerType type;
	private WhenConditionNode whenConditionNode;
	private ActionNode exceptionAction;
	private ActionNode[] targetActions;
	
	
	public TriggerNode() {
		
	}

	public TriggerNode(String name, TriggerType type, WhenConditionNode whenConditionNode, ActionNode[] targetActions, ActionNode exceptionAction) {
		this.name = name;
		this.type = type;
		this.whenConditionNode = whenConditionNode;
		this.targetActions = targetActions;
		this.exceptionAction = exceptionAction;
	}


	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TriggerType getType() {
		return type;
	}

	public void setType(TriggerType type) {
		this.type = type;
	}


	public ActionNode getExceptionAction() {
		return exceptionAction;
	}

	public void setExceptionAction(ActionNode exceptionAction) {
		this.exceptionAction = exceptionAction;
	}

	public ActionNode[] getTargetActions() {
		return targetActions;
	}

	public void setTargetActions(ActionNode[] targetActions) {
		this.targetActions = targetActions;
	}

	public WhenConditionNode getWhenConditionNode() {
		return whenConditionNode;
	}

	public void setWhenConditionNode(WhenConditionNode whenConditionNode) {
		this.whenConditionNode = whenConditionNode;
	}



}
