package br.com.anteros.iot.things.parts;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.parts.RedLEDSemaphorePartNode;
import br.com.anteros.iot.parts.exception.IllegalPartException;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.things.Semaphore;

public class RedLEDSemaphorePart extends PlantItem implements Part, LedSemaphore {

	protected int pin;
	protected DeviceController deviceController;

	private RedLEDSemaphorePart(String id, Semaphore owner, int pin) {
		this.itemOwner = owner;
		this.pin = pin;
		this.itemId = id;
	}

	public RedLEDSemaphorePart(RedLEDSemaphorePartNode node) {
		this.pin = node.getPin();
		this.itemId = node.getItemName();
	}

	public String getThingID() {
		return itemId;
	}

	public ThingStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Part> getParts() {
		return Collections.unmodifiableSet(new HashSet<Part>());
	}

	public boolean hasParts() {
		return false;
	}

	public Thing addPart(Part part) {
		throw new IllegalPartException("Esta parte não permite a composição com mais partes.");
	}

	public Thing removePart(Part part) {
		throw new IllegalPartException("Esta parte não permite a composição com mais partes.");
	}

	@Override
	public Thing getOwner() {
		return (Thing) itemOwner;
	}
	
	public static RedLEDSemaphorePart of(String id, Semaphore semaphore, int pin) {
		return new RedLEDSemaphorePart(id,semaphore, pin) ;
	}

	public int getPin() {
		return pin;
	}

	public Part getPartById(String part) {
		return null;
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
