package br.com.anteros.iot.things;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.AccessType;
import br.com.anteros.iot.domain.things.DigitalKeyNode;
import br.com.anteros.iot.triggers.Trigger;

public class DigitalKey extends ControllerThing implements Publishable {

	protected DeviceController deviceController;

	protected boolean needsPropagation;
	
	protected String[] topics;
	protected AccessType accessType;
	protected Set<Trigger> triggers = new HashSet<>();
	protected String status;
	protected String lastValue;

	public DigitalKey(PlantItemNode node) {
		this.loadConfiguration(node);
	}

	public String getThingID() {
		return itemId;
	}

	public String getStatus() {
		return null;
	}
	
	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	@Override
	public String getLastValue() {
		return lastValue;
	}

	@Override
	public void setLastValue(String value) {
		this.lastValue = value;
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
		this.accessType = ((DigitalKeyNode) node).getAccessType();
		this.needsPropagation = ((DigitalKeyNode) node).needsPropagation();
		this.topics = ((DigitalKeyNode) node).getTopics();
		this.hostMqtt = ((DigitalKeyNode)node).getHostMqtt();
		this.hostNtp = ((DigitalKeyNode)node).getHostNtp();
		this.passwordMqtt = ((DigitalKeyNode)node).getPasswordMqtt();
		this.portMqtt = ((DigitalKeyNode)node).getPortMqtt();
		this.primaryPassword = ((DigitalKeyNode)node).getPrimaryPassword();
		this.primarySSID = ((DigitalKeyNode)node).getPrimarySSID();
		this.secondaryPassword = ((DigitalKeyNode)node).getSecondaryPassword();
		this.secondarySSID = ((DigitalKeyNode)node).getSecondarySSID();
		this.needsPropagation = ((DigitalKeyNode)node).needsPropagation();
		this.timezoneNtp = ((DigitalKeyNode)node).getTimezoneNtp();
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
	public String getThingType() {
		return DomainConstants.DIGITAL_KEY;
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
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}

	@Override
	public String toString() {
		return "DigitalKey [deviceController=" + deviceController + ", needsPropagation=" + needsPropagation
				+ ", topics=" + Arrays.toString(topics) + ", triggers=" + triggers + ", primarySSID=" + primarySSID
				+ ", primaryPassword=" + primaryPassword + ", secondarySSID=" + secondarySSID + ", secondaryPassword="
				+ secondaryPassword + ", hostNtp=" + hostNtp + ", timezoneNtp=" + timezoneNtp + ", hostMqtt=" + hostMqtt
				+ ", portMqtt=" + portMqtt + ", userMqtt=" + userMqtt + ", passwordMqtt=" + passwordMqtt
				+ ", itemOwner=" + itemOwner + ", items=" + items + ", itemId=" + itemId + ", description="
				+ description + "]";
	}

	
}
