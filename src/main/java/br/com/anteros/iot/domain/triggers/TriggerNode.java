package br.com.anteros.iot.domain.triggers;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.actions.ActionNode;
import br.com.anteros.iot.triggers.ShotMoment;

public class TriggerNode extends PlantItemNode {
	
	private String name;
	private ShotMoment shotMoment;
	private WhenConditionNode whenConditionNode;
	private ActionNode[] exceptionActions;
	private ActionNode[] targetActions;
	private boolean requiresPermission;
	
	
	public TriggerNode() {
		
	}

	public TriggerNode(String name, ShotMoment shotMoment, WhenConditionNode whenConditionNode, ActionNode[] targetActions, ActionNode[] exceptionActions) {
		this.name = name;
		this.shotMoment = shotMoment;
		this.whenConditionNode = whenConditionNode;
		this.targetActions = targetActions;
		this.exceptionActions = exceptionActions;
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

	public ActionNode[] getExceptionActions() {
		return exceptionActions;
	}

	public void setExceptionActions(ActionNode[] exceptionActions) {
		this.exceptionActions = exceptionActions;
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

	public ShotMoment getShotMoment() {
		return shotMoment;
	}

	public void setShotMoment(ShotMoment shotMoment) {
		this.shotMoment = shotMoment;
	}

	public boolean isRequiresPermission() {
		return requiresPermission;
	}

	public void setRequiresPermission(boolean requiresPermission) {
		this.requiresPermission = requiresPermission;
	}

}
