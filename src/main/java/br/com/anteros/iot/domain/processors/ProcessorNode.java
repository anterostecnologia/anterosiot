package br.com.anteros.iot.domain.processors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.actuators.processors.Processor;
import br.com.anteros.iot.domain.PlantItemNode;

public abstract class ProcessorNode<T extends CollectResult> extends PlantItemNode {
	
	
	public ProcessorNode() {
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return null;
	}
	
	@JsonIgnore
	public abstract Processor<T> getInstanceOfProcessor();
}
