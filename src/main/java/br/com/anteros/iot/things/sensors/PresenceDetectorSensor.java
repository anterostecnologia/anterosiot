package br.com.anteros.iot.things.sensors;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Sensor;
import br.com.anteros.iot.SensorCollectionType;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.collectors.CollectResult;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.PresenceDetectorNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.triggers.Trigger;

public class PresenceDetectorSensor extends PlantItem implements Sensor {
	
	protected int pin;
	protected String[] topics;
	protected DeviceController deviceController;
	protected Set<Trigger> triggers = new HashSet<>();

	public PresenceDetectorSensor(String itemId, int pin, String[] topics) {
		super();
		this.itemId = itemId;
		this.pin = pin;
		this.topics =  topics;
	}


	public PresenceDetectorSensor(PresenceDetectorNode node) {
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		this.pin = node.getPin();
		this.topics =  node.getTopics();
	}


	@Override
	public String getThingID() {
		return itemId;
	}

	@Override
	public ThingStatus getStatus() {
		return null;
	}

	@Override
	public Set<Part> getParts() {
		return Collections.unmodifiableSet(new HashSet<>());
	}

	@Override
	public boolean hasParts() {
		return false;
	}

	@Override
	public Thing addPart(Part part) {
		return this;
	}

	@Override
	public Thing removePart(Part part) {
		return this;
	}

	@Override
	public Part getPartById(String part) {
		return null;
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		return this;
	}

	@Override
	public long getTimeIntervalForCollect() {
		return 0;
	}

	@Override
	public SensorCollectionType getCollectionType() {
		return SensorCollectionType.EVENT;
	}

	@Override
	public String[] getTopicsToPublishValue(CollectResult collectedData) {
		if (topics ==null || topics.length==0) {
			return new String[] {this.getPath()};
		}
		return topics;
	}

	public String getThingId() {
		return itemId;
	}

	public int getPin() {
		return pin;
	}

	public String[] getTopics() {
		return topics;
	}

	@Override
	protected boolean acceptThisTypeOfPlantItem(Class<?> child) {
		// TODO Auto-generated method stub
		return false;
	}


	public DeviceController getDeviceController() {
		return deviceController;
	}


	public void setDeviceController(DeviceController deviceController) {
		this.deviceController = deviceController;
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
		// TODO Auto-generated method stub
		return null;
	}

}
