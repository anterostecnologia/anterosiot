package br.com.anteros.iot.things;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.things.exception.ThingException;
import br.com.anteros.iot.things.parts.GreenLEDSemaphorePart;
import br.com.anteros.iot.things.parts.LedSemaphore;
import br.com.anteros.iot.things.parts.RedLEDSemaphorePart;
import br.com.anteros.iot.things.parts.YellowLEDSemaphorePart;
import br.com.anteros.iot.triggers.Trigger;

public class Semaphore extends PlantItem implements Thing {

	protected DeviceController deviceController;
	protected Set<Part> leds = new LinkedHashSet<Part>();
	protected Set<Trigger> triggers = new HashSet<>();

	public Semaphore(String id) {
		this.itemId = id;
	}

	public Semaphore() {
	}

	public Semaphore(PlantItemNode node) {
		loadConfiguration(node);
	}

	public String getThingID() {
		return itemId;
	}

	public ThingStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Part> getParts() {
		return leds;
	}

	public boolean hasParts() {
		return !leds.isEmpty();
	}

	public Thing addPart(Part part) {
		if (!(part instanceof LedSemaphore)) {
			throw new ThingException("Tipo de parte inválida para uso com Semaphore.");
		}
		if (part instanceof PlantItem) {
			((PlantItem) part).setItemOwner(this);
		}
		leds.add(part);
		return this;
	}

	public Thing removePart(Part part) {
		leds.remove(part);
		return this;
	}

	public Part getPartById(String part) {
		for (Part p : leds) {
			if (p.getThingID().equals(part)) {
				return p;
			}
		}
		return null;
	}

	public static Semaphore of(PlantItemNode node) {
		return new Semaphore(node);
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		for (PlantItemNode child : node.getItems()) {
			if (child instanceof ThingNode) {
				Thing part = child.getInstanceOfThing();
				((PlantItem) part).setItemOwner(this);
				if (part instanceof Part) {
					this.addPart((Part) part);
				}
			}
		}
		return this;
	}

	@Override
	protected boolean acceptThisTypeOfPlantItem(Class<?> child) {
		return child.equals(RedLEDSemaphorePart.class) || child.equals(GreenLEDSemaphorePart.class)
				|| child.equals(YellowLEDSemaphorePart.class);
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
