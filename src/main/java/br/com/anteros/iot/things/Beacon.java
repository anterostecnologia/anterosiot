package br.com.anteros.iot.things;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.BeaconNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.triggers.Trigger;

public class Beacon extends PlantItem implements Thing {
	
	protected DeviceController deviceController;
	protected Set<Trigger> triggers = new HashSet<>();
	protected String[] topics;

	public Beacon() {
	}

	public Beacon(BeaconNode node) {
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		this.topics = node.getTopics();
	}

	public String getThingID() {
		return itemId;
	}

	public ThingStatus getStatus() {
		return null;
	}

	public Set<Part> getParts() {
		return null;
	}

	public boolean hasParts() {
		return false;
	}

	public Thing addPart(Part part) {
		return null;
	}

	public Thing removePart(Part part) {
		return null;
	}

	public Part getPartById(String part) {
		return null;
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		return null;
	}

	@Override
	protected boolean acceptThisTypeOfPlantItem(Class<?> child) {
		return false;
	}

	public DeviceController getDeviceController() {
		return deviceController;
	}

	public void setDeviceController(DeviceController deviceController) {
		this.deviceController = deviceController;
	}

	@Override
	public String[] getActions() {
		return null;
	}

	@Override
	public Trigger[] getTriggers() {
		return triggers.toArray(new Trigger[] {});
	}


	@Override
	public Thing addTrigger(Trigger trigger) {
		triggers.add(trigger);
		return this;
	}

	@Override
	public Thing removeTrigger(Trigger trigger) {
		triggers.remove(trigger);
		return this;
	}

	public void setTriggers(Set<Trigger> triggers) {
		this.triggers = triggers;
	}

	@Override
	public String toString() {
		return "Beacon [topics=" + Arrays.toString(topics) + ", itemId=" + itemId + ", description=" + description
				+ "]";
	}

	
}
