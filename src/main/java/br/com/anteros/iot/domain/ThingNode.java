package br.com.anteros.iot.domain;

import java.util.HashSet;
import java.util.Set;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.domain.triggers.TriggerNode;
import br.com.anteros.iot.triggers.Trigger;

public abstract class ThingNode extends PlantItemNode {

	private Set<TriggerNode> triggers = new HashSet<>();

	public ThingNode() {
		super();
	}

	public ThingNode(String itemName, String description) {
		super(itemName, description);
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return child.equals(ThingNode.class) || ReflectionUtils.isExtendsClass(ThingNode.class, child);
	}

	public ThingNode addTrigger(TriggerNode trigger) {
		this.triggers.add(trigger);
		return this;
	}

	public ThingNode removeTrigger(TriggerNode trigger) {
		this.triggers.remove(trigger);
		return this;
	}

	public Set<TriggerNode> getTriggers() {
		return this.triggers;
	}

}
