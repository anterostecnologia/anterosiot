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
import br.com.anteros.iot.domain.things.RFIDModel;
import br.com.anteros.iot.domain.things.RFIDReaderNode;
import br.com.anteros.iot.triggers.Trigger;

public class RFIDReader extends ControllerThing implements Publishable {

	protected DeviceController deviceController;
	protected boolean needsPropagation;	
	protected String[] topics;
	protected RFIDModel model;
	protected int sspin;
	protected Set<Trigger> triggers = new HashSet<>();

	public RFIDReader(PlantItemNode node) {
		this.loadConfiguration(node);
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
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		this.topics = ((RFIDReaderNode)node).getTopics();
		this.hostMqtt = ((RFIDReaderNode)node).getHostMqtt();
		this.hostNtp = ((RFIDReaderNode)node).getHostNtp();
		this.passwordMqtt = ((RFIDReaderNode)node).getPasswordMqtt();
		this.portMqtt = ((RFIDReaderNode)node).getPortMqtt();
		this.primaryPassword = ((RFIDReaderNode)node).getPrimaryPassword();
		this.primarySSID = ((RFIDReaderNode)node).getPrimarySSID();
		this.secondaryPassword = ((RFIDReaderNode)node).getSecondaryPassword();
		this.secondarySSID = ((RFIDReaderNode)node).getSecondarySSID();
		this.needsPropagation = ((RFIDReaderNode)node).needsPropagation();
		this.timezoneNtp = ((RFIDReaderNode)node).getTimezoneNtp();
		this.model = ((RFIDReaderNode) node).getModel();
		this.topics = ((RFIDReaderNode) node).getTopics();
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

	public RFIDModel getModel() {
		return model;
	}

	public void setModel(RFIDModel model) {
		this.model = model;
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
		return DomainConstants.RFID_NODE;
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
		return "RFIDReader [topics=" + Arrays.toString(topics) + ", model=" + model + ", itemId=" + itemId
				+ ", description=" + description + "]";
	}

	@Override
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}

	public int getSspin() {
		return sspin;
	}

	public void setSspin(int sspin) {
		this.sspin = sspin;
	}
	
}
