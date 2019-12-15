package br.com.anteros.iot.things;

import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.BarrierSensorReaderNode;
import br.com.anteros.iot.triggers.Trigger;

public class BarrierSensorReader extends ControllerThing {
	
	protected DeviceController deviceController;
	protected boolean needsPropagation;
	protected Set<Trigger> triggers = new HashSet<>();
	protected String[] topics;
	

	public BarrierSensorReader() {
	}

	public BarrierSensorReader(BarrierSensorReaderNode node) {
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		this.topics = node.getTopics();
		this.hostMqtt = node.getHostMqtt();
		this.hostNtp = node.getHostNtp();
		this.passwordMqtt = node.getPasswordMqtt();
		this.portMqtt = node.getPortMqtt();
		this.primaryPassword = node.getPrimaryPassword();
		this.primarySSID = node.getPrimarySSID();
		this.secondaryPassword = node.getSecondaryPassword();
		this.secondarySSID = node.getSecondarySSID();
		this.needsPropagation = node.needsPropagation();
		this.timezoneNtp = node.getTimezoneNtp();
	}

	public String getThingID() {
		return itemId;
	}

	public ThingStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Part> getParts() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasParts() {
		// TODO Auto-generated method stub
		return false;
	}

	public Thing addPart(Part part) {
		// TODO Auto-generated method stub
		return null;
	}

	public Thing removePart(Part part) {
		// TODO Auto-generated method stub
		return null;
	}

	public Part getPartById(String part) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
	

	public void setTriggers(Set<Trigger> triggers) {
		this.triggers = triggers;
	}

	
	

}