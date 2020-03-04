package br.com.anteros.iot.actions;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.PlantItemNode;

public class ExecutionCondition extends PlantItemNode {
	private Target target;
	private Condition condition;
	private String value;

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public boolean setConditionByString(String condition) {
		if (condition.equalsIgnoreCase(Condition.NONE.toString())) {
			this.condition = Condition.NONE;
			return true;
		}
		if (condition.equalsIgnoreCase(Condition.EQUAL.toString())) {
			this.condition = Condition.EQUAL;
			return true;
		}
		if (condition.equalsIgnoreCase(Condition.NOT_EQUAL.toString())) {
			this.condition = Condition.NOT_EQUAL;
			return true;
		}
		return false;
	}

	public boolean setTargetByString(String target) {
		if (target.equalsIgnoreCase(Target.STATUS.toString())) {
			this.target = Target.STATUS;
			return true;
		}
		return false;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String status) {
		this.value = status;
	}

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return null;
	}

}
