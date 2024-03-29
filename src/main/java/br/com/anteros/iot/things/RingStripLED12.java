package br.com.anteros.iot.things;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.RingStripLED12Node;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.support.colors.RGB;
import br.com.anteros.iot.things.test.LEDDisplayType;
import br.com.anteros.iot.triggers.Trigger;

public class RingStripLED12 extends PlantItem implements Thing {
	
	protected DeviceController deviceController;
	protected boolean needsPropagation;
	protected int pin;
	protected LEDDisplayType ledType;
	protected int animateMiliseconds;
	protected RGB color;
	protected int brightness = 64;	
	protected int numPixels = 60;
	protected Set<Trigger> triggers = new LinkedHashSet<>();
	protected Object userData;
	protected String status;
	protected String lastValue;
	
	protected RingStripLED12(String id, int pin, LEDDisplayType ledType, int animateMiliseconds,
			int brightness, int numPixels ) {
		this.itemId = id;
		this.pin = pin;
		this.ledType = ledType;
		this.numPixels = numPixels;
		this.animateMiliseconds = animateMiliseconds;
		this.brightness = brightness;
	}
	
	protected RingStripLED12(String id, int pin, LEDDisplayType ledType, int animateMiliseconds) {
		this.itemId = id;
		this.pin = pin;
		this.ledType = ledType;
		this.animateMiliseconds = animateMiliseconds;
	}

	public RingStripLED12(RingStripLED12Node node) {
		this.pin = node.getPin();
		this.itemId = node.getItemName();
		this.pin = node.getPin();
		this.animateMiliseconds = node.getAnimateMiliseconds();
		this.color = node.getColor();
		this.ledType = node.getLedType();
		this.brightness = node.getBrightness();
		this.numPixels = node.getNumPixels();
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
		return DomainConstants.RING_STRIP_LED12;
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

	public LEDDisplayType getLedType() {
		return ledType;
	}

	public void setLedType(LEDDisplayType ledType) {
		this.ledType = ledType;
	}

	public int getAnimateMiliseconds() {
		return animateMiliseconds;
	}

	public void setAnimateMiliseconds(int animateMiliseconds) {
		this.animateMiliseconds = animateMiliseconds;
	}

	public RGB getColor() {
		return color;
	}

	public void setColor(RGB color) {
		this.color = color;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public void setTriggers(Set<Trigger> triggers) {
		this.triggers = triggers;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public int getNumPixels() {
		return numPixels;
	}

	public void setNumPixels(int numPixels) {
		this.numPixels = numPixels;
	}

	@Override
	public String toString() {
		return "RingStripLED12 [pin=" + pin + ", ledType=" + ledType + ", animateMiliseconds=" + animateMiliseconds
				+ ", color=" + color + ", brightness=" + brightness + ", numPixels=" + numPixels + ", itemId=" + itemId
				+ ", description=" + description + "]";
	}

	@Override
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}

}
