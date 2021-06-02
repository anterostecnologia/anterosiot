package br.com.anteros.iot.things;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.BeaconNode;
import br.com.anteros.iot.triggers.Trigger;

public class Beacon extends ControllerThing {
	
	protected DeviceController deviceController;
	protected boolean needsPropagation;
	protected Set<Trigger> triggers = new HashSet<>();
	protected String[] topics;

	

	public Beacon() {
	}

	public Beacon(BeaconNode node) {
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		this.topics = node.getTopics();
		this.needsPropagation = ((BeaconNode) node).needsPropagation();
		this.topics = ((BeaconNode) node).getTopics();
		this.hostMqtt = ((BeaconNode) node).getHostMqtt();
		this.hostNtp = ((BeaconNode) node).getHostNtp();
		this.passwordMqtt = ((BeaconNode) node).getPasswordMqtt();
		this.portMqtt = ((BeaconNode) node).getPortMqtt();
		this.primaryPassword = ((BeaconNode) node).getPrimaryPassword();
		this.primarySSID = ((BeaconNode) node).getPrimarySSID();
		this.secondaryPassword = ((BeaconNode) node).getSecondaryPassword();
		this.secondarySSID = ((BeaconNode) node).getSecondarySSID();
		this.needsPropagation = ((BeaconNode) node).needsPropagation();
		this.timezoneNtp = ((BeaconNode) node).getTimezoneNtp();
	}

	public String getThingID() {
		return itemId;
	}

	public String getStatus() {
		return null;
	}
	
	public void setStatus(java.lang.String status) {
		
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
	public String getThingType() {
		return DomainConstants.BEACON;
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
	
	@Override
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}

	public boolean isNeedsPropagation() {
		return needsPropagation;
	}

	public void setNeedsPropagation(boolean needsPropagation) {
		this.needsPropagation = needsPropagation;
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	public String getPrimarySSID() {
		return primarySSID;
	}

	public void setPrimarySSID(String primarySSID) {
		this.primarySSID = primarySSID;
	}

	public String getPrimaryPassword() {
		return primaryPassword;
	}

	public void setPrimaryPassword(String primaryPassword) {
		this.primaryPassword = primaryPassword;
	}

	public String getSecondarySSID() {
		return secondarySSID;
	}

	public void setSecondarySSID(String secondarySSID) {
		this.secondarySSID = secondarySSID;
	}

	public String getSecondaryPassword() {
		return secondaryPassword;
	}

	public void setSecondaryPassword(String secondaryPassword) {
		this.secondaryPassword = secondaryPassword;
	}

	
}
