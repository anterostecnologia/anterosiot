package br.com.anteros.iot.things;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.LampOrBulbNode;
import br.com.anteros.iot.plant.PlantItem;

public class LampOrBulb extends PlantItem implements Thing {
	
	protected DeviceController deviceController;
	protected int pin;

	protected LampOrBulb(String id, int pin) {
		this.itemId = id;
		this.pin = pin;
	}

	public LampOrBulb(LampOrBulbNode node) {
		this.pin = node.getPin();
		this.itemId = node.getItemName();
		this.pin = node.getPin();
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
		// TODO Auto-generated method stub
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
