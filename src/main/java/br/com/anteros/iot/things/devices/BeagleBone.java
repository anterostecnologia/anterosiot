package br.com.anteros.iot.things.devices;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.triggers.Trigger;

public class BeagleBone extends PlantItem implements Device {
	
	private IpAddress ipAddress;
	protected DeviceController deviceController;

	public BeagleBone(String id, IpAddress ipAddress) {
		this.itemId = id;
		this.ipAddress = ipAddress;
	}

	public String getThingID() {
		return itemId;
	}

	public ThingStatus getStatus() {
		return null;
	}

	public Set<Part> getParts() {
		return Collections.unmodifiableSet(new HashSet<Part>());
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

	public IpAddress getIpAddress() {
		return ipAddress;
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
	public Device setIpAddress(IpAddress ipAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean acceptThisTypeOfPlantItem(Class<?> child) {
		return ReflectionUtils.isImplementsInterface(child, Thing.class) && !ReflectionUtils.isImplementsInterface(child, Part.class) 
				&& !ReflectionUtils.isImplementsInterface(child, Device.class);
	}

	public DeviceController getDeviceController() {
		return deviceController;
	}

	public void setDeviceController(DeviceController deviceController) {
		this.deviceController = deviceController;
	}
	
	@Override
	public String[] getActions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Thing addTrigger(Trigger trigger) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Thing removeTrigger(Trigger trigger) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Trigger[] getTriggers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathError() {
		// TODO Auto-generated method stub
		return null;
	}

}
