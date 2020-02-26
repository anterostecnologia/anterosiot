package br.com.anteros.iot.things;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.VehicleEntranceTriggerNode;
import br.com.anteros.iot.triggers.Trigger;

public class VehicleEntranceTrigger extends ControllerThing implements Publishable {

	protected DeviceController deviceController;

	protected boolean needsPropagation;
	
	protected String[] topics;
	protected Set<Trigger> triggers = new HashSet<>();

	public VehicleEntranceTrigger(PlantItemNode node) {
		this.loadConfiguration(node);
	}

	public String getThingID() {
		return itemId;
	}

	public ThingStatus getStatus() {
		return null;
	}

	public Set<Part> getParts() {
		return Collections.unmodifiableSet(new HashSet<>());
	}

	public boolean hasParts() {
		return false;
	}

	public Thing addPart(Part part) {
		return this;
	}

	public Thing removePart(Part part) {
		return this;
	}

	public Part getPartById(String part) {
		return null;
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		this.needsPropagation = ((VehicleEntranceTriggerNode) node).needsPropagation();
		this.topics = ((VehicleEntranceTriggerNode) node).getTopics();
		this.hostMqtt = ((VehicleEntranceTriggerNode)node).getHostMqtt();
		this.hostNtp = ((VehicleEntranceTriggerNode)node).getHostNtp();
		this.passwordMqtt = ((VehicleEntranceTriggerNode)node).getPasswordMqtt();
		this.portMqtt = ((VehicleEntranceTriggerNode)node).getPortMqtt();
		this.primaryPassword = ((VehicleEntranceTriggerNode)node).getPrimaryPassword();
		this.primarySSID = ((VehicleEntranceTriggerNode)node).getPrimarySSID();
		this.secondaryPassword = ((VehicleEntranceTriggerNode)node).getSecondaryPassword();
		this.secondarySSID = ((VehicleEntranceTriggerNode)node).getSecondarySSID();
		this.needsPropagation = ((VehicleEntranceTriggerNode)node).needsPropagation();
		this.timezoneNtp = ((VehicleEntranceTriggerNode)node).getTimezoneNtp();
		return this;
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
	public String[] getTopicsToPublishValue(CollectResult collectedData) {
		if (topics == null || topics.length == 0) {
			return new String[] { this.getPath() };
		}
		return topics;
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

	@Override
	public String[] getActions() {
		return null;
	}

	@Override
	public String toString() {
		return "GateTrigger [topics=" + Arrays.toString(topics) + ", itemId=" + itemId
				+ ", description=" + description + "]";
	}

	@Override
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}

	
}
