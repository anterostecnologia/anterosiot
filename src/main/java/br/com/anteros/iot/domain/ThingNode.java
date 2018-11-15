package br.com.anteros.iot.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.domain.processors.ProcessorNode;
import br.com.anteros.iot.domain.triggers.TriggerNode;

public abstract class ThingNode extends PlantItemNode {

	private Set<TriggerNode> triggers = new HashSet<>();
	private List<ProcessorNode<? extends CollectResult>> processors = new ArrayList<>();

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

	public List<ProcessorNode<? extends CollectResult>> getProcessors() {
		return processors;
	}
	
	public ThingNode addProcessor(ProcessorNode<? extends CollectResult> processor) {
		this.processors.add(processor);
		return this;
	}

	public ThingNode removeProcessor(ProcessorNode<? extends CollectResult> processor) {
		this.processors.remove(processor);
		return this;
	}

}
