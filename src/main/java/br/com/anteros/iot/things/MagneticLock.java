package br.com.anteros.iot.things;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.MagneticLockNode;
import br.com.anteros.iot.plant.PlantItem;

public class MagneticLock extends PlantItem implements Thing {
	
	protected DeviceController deviceController;
	
	protected int pin;
	protected int timeWaitOpening;

	protected MagneticLock(String id, int pin, int timeWaitOpening) {
		this.itemId = id;
		this.pin = pin;
		this.timeWaitOpening = timeWaitOpening;
	}

	public MagneticLock(MagneticLockNode node) {
		this.pin = node.getPin();
		this.timeWaitOpening = node.getTimeWaitOpening();
		this.itemId = node.getItemName();
	}

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

	public int getPin() {
		return pin;
	}
	
	public long getTimeWaitOpening() {
		return timeWaitOpening;
	}

	public static MagneticLock of(String id, int pin, int timeWaitOpening) {
		return new MagneticLock(id, pin, timeWaitOpening);
	}

	@Override
	public String toString() {
		return "MagneticLock [ itemId=" + itemId + ", pin=" + pin + ", timeWaitOpening="
				+ timeWaitOpening + "]";
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		// TODO Auto-generated method stub
		return null;
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

}
