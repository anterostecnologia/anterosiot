package br.com.anteros.iot.things.parts;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.parts.GreenLEDSemaphorePartNode;
import br.com.anteros.iot.parts.exception.IllegalPartException;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.things.Semaphore;
import br.com.anteros.iot.triggers.Trigger;

public class GreenLEDSemaphorePart extends PlantItem implements Part, LedSemaphore {

	protected int pin;
	protected String thingId;
	protected DeviceController deviceController;
	protected boolean needsPropagation;
	protected Set<Trigger> triggers = new HashSet<>();
	private Object userData;
	protected String status;
	protected String lastValue;

	private GreenLEDSemaphorePart(String id, Semaphore owner, int pin) {
		this.itemOwner = owner;
		this.pin = pin;
		this.thingId = id;
	}

	public GreenLEDSemaphorePart(GreenLEDSemaphorePartNode node) {
		this.thingId = node.getItemName();
		this.pin = node.getPin();

	}

	public String getThingID() {
		return thingId;
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

	public static GreenLEDSemaphorePart of(String id, Semaphore semaphore, int pin) {
		return new GreenLEDSemaphorePart(id, semaphore, pin);
	}

	public int getPin() {
		return pin;
	}

	public Part getPartById(String part) {
		return null;
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		return null;
	}

	public String getThingId() {
		return thingId;
	}

	public void setThingId(String thingId) {
		this.thingId = thingId;
	}

	public void setPin(int pin) {
		this.pin = pin;
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
		return DomainConstants.GREEN_LED_SEMAPHORE;
	}

	@Override
	protected boolean acceptThisTypeOfPlantItem(Class<?> child) {
		return false;
	}

	@Override
	public Thing getOwner() {
		return (Thing) itemOwner;
	}

	public DeviceController getDeviceController() {
		return deviceController;
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
	public void setDeviceController(DeviceController deviceController) {
		this.deviceController = deviceController;
		
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
