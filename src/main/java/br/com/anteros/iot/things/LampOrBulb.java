package br.com.anteros.iot.things;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.LampOrBulbNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.triggers.Trigger;

public class LampOrBulb extends PlantItem implements Thing {
	
	protected DeviceController deviceController;
	protected boolean needsPropagation;
	protected int pin;
	protected Set<Trigger> triggers = new HashSet<>();
	protected Object userData;
	protected String status;
	protected String lastValue;

	protected LampOrBulb(String id, int pin) {
		this.itemId = id;
		this.pin = pin;
	}

	public LampOrBulb(LampOrBulbNode node) {
		this.pin = node.getPin();
		this.itemId = node.getItemName();
		this.pin = node.getPin();
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
		return DomainConstants.LAMP;
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
		return null;
	}

	public Part getPartById(String part) {
		return null;
	}

	public int getPin() {
		return pin;
	}

	public static LampOrBulb of(String id, int pin) {
		return new LampOrBulb(id, pin);
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
	public String toString() {
		return "LampOrBulb [pin=" + pin + ", itemId=" + itemId + ", description=" + description + "]";
	}
	
	@Override
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}
	
}
