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
import br.com.anteros.iot.actuators.processors.Processor;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.things.devices.telemetry.TelemetryStrategy;
import br.com.anteros.iot.triggers.Trigger;

public class Computer extends PlantItem implements Device {
	
	private IpAddress ipAddress;
	private String pathError;
	
	protected DeviceController deviceController;

	private Computer(String id, IpAddress ipAddress, String description, String pathError, AbstractDeviceController controller) {
		this.itemId = id;
		this.ipAddress = ipAddress;
		this.description = description;
		this.pathError = pathError == null ? "": pathError;
		this.deviceController = controller;
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
		this.ipAddress = ipAddress;
		return this;
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
	
	public String getPathError() {
		if ("".equals(pathError)) {
			return this.getPath() + "/error";
		}
		return pathError;
	}

	public static Device of(String deviceName, IpAddress ipAddress, String description, String pathError, AbstractDeviceController controller) {
		return new Computer(deviceName, ipAddress, description, pathError, controller);
	}

	public void setPathError(String pathError) {
		this.pathError = pathError;
	}


	@Override
	public TelemetryStrategy[] getTelemetries() {
		return null;
	}

	@Override
	public boolean hasTelemetries() {
		return false;
	}

	@Override
	public TelemetryStrategy[] getTelemetriesByInterval(long ellapsedTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Thing addProcessor(Processor<?> processor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Thing removeProcessor(Processor<?> processor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Processor<?>[] getProcessors() {
		// TODO Auto-generated method stub
		return null;
	}
}
