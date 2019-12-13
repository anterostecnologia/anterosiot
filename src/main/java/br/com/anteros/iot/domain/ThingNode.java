package br.com.anteros.iot.domain;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.domain.plant.PlaceNode;
import br.com.anteros.iot.domain.triggers.TriggerNode;

public abstract class ThingNode extends PlantItemNode {

	private Set<TriggerNode> triggers = new HashSet<>();
	protected boolean needsPropagation;

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

	public abstract String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException;

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

	public void setNeedsPropagation(boolean needsPropagation) {
		this.needsPropagation = needsPropagation;
	}

	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}

	public DeviceNode getDeviceNode() {
		return findDeviceNode(itemNodeOwner);
	}

	private DeviceNode findDeviceNode(PlantItemNode itemNodeOwner) {
		if (itemNodeOwner != null) {
			if (itemNodeOwner instanceof DeviceNode) {
				return (DeviceNode) itemNodeOwner;
			}

			return findDeviceNode(itemNodeOwner.getItemNodeOwner());
		}
		
		return null;
	}
	
	public PlaceNode getPlaceNode() {
		return findPlaceNode(itemNodeOwner);
	}

	private PlaceNode findPlaceNode(PlantItemNode itemNodeOwner) {
		if (itemNodeOwner != null) {
			if (itemNodeOwner instanceof PlaceNode) {
				return (PlaceNode) itemNodeOwner;
			}
			return findPlaceNode(itemNodeOwner.getItemNodeOwner());
		}
		
		return null;
	}


}
