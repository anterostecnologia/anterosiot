package br.com.anteros.iot.things;

import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.triggers.Trigger;

public class TicketGate extends PlantItem implements Thing {

	protected DeviceController deviceController;
	protected Set<Trigger> triggers = new HashSet<>();

	public TicketGate() {
		// TODO Auto-generated constructor stub
	}

	public String getThingID() {
		return itemId;
	}

	public ThingStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Part> getParts() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasParts() {
		// TODO Auto-generated method stub
		return false;
	}

	public Thing addPart(Part part) {
		// TODO Auto-generated method stub
		return null;
	}

	public Thing removePart(Part part) {
		// TODO Auto-generated method stub
		return null;
	}

	public Part getPartById(String part) {
		// TODO Auto-generated method stub
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
