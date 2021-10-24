package br.com.anteros.iot.things.sensors;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.domain.DomainConstants;
import com.pi4j.temperature.TemperatureScale;

import br.com.anteros.core.utils.ArrayUtils;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Sensor;
import br.com.anteros.iot.SensorCollectionType;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.TemperatureOneWireNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.triggers.Trigger;

public class TemperatureSensorOneWire extends PlantItem implements Sensor {

	protected String sensorId;
	protected String[] topics;
	protected TemperatureScale scale;
	protected DeviceController deviceController;
	protected boolean needsPropagation;
	protected Set<Trigger> triggers = new HashSet<>();
	private Object userData;
	protected String status;
	protected String lastValue;

	protected TemperatureSensorOneWire(String id, String sensorId, String[] topics,
			TemperatureScale scale) {
		this.itemId = id;
		this.sensorId = sensorId;
		this.topics =  topics;
		this.scale = scale;
	}

	public TemperatureSensorOneWire(TemperatureOneWireNode node) {
		this.sensorId = node.getSensorId();
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		this.topics =  node.getTopics();
		this.scale = node.getScale();
	}

	public String getThingID() {
		return itemId;
	}

	public String getStatus() {
		return status;
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

	@Override
	public Object getUserData() {
		return userData;
	}

	@Override
	public void setUserData(Object data){
		this.userData = data;
	}

	@Override
	public String getThingType() {
		return DomainConstants.TEMPERATURE_ONE_WIRE;
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
		return null;
	}


	@Override
	public SensorCollectionType getCollectionType() {
		return SensorCollectionType.TEMPORAL;
	}

	@Override
	public String[] getTopicsToPublishValue(CollectResult collectedData) {
		if (topics ==null || topics.length==0) {
			return new String[] {this.getPath()};
		}
		return topics;
	}

	public TemperatureScale getTemperatureScale() {
		return scale;
	}

	public String getSensorId() {
		return sensorId;
	}

	@Override
	public long getTimeIntervalForCollect() {
		return 0;
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
	
	@Override
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}
	
}
