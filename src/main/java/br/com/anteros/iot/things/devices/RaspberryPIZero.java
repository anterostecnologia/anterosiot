package br.com.anteros.iot.things.devices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.plant.Place;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.things.Publishable;
import br.com.anteros.iot.things.devices.telemetry.ClockTelemetryStrategy;
import br.com.anteros.iot.things.devices.telemetry.CodecTelemetryStrategy;
import br.com.anteros.iot.things.devices.telemetry.HardwareTelemetryStrategy;
import br.com.anteros.iot.things.devices.telemetry.JavaTelemetryStrategy;
import br.com.anteros.iot.things.devices.telemetry.MemoryTelemetryStrategy;
import br.com.anteros.iot.things.devices.telemetry.NetworkTelemetryStrategy;
import br.com.anteros.iot.things.devices.telemetry.PlatformTelemetryStrategy;
import br.com.anteros.iot.things.devices.telemetry.SOTelemetryStrategy;
import br.com.anteros.iot.things.devices.telemetry.TelemetryConfiguration;
import br.com.anteros.iot.things.devices.telemetry.TelemetryStrategy;
import br.com.anteros.iot.things.devices.telemetry.TemperatureTelemetryStrategy;
import br.com.anteros.iot.triggers.Trigger;

public class RaspberryPIZero extends PlantItem implements Device, Publishable  {
	
	protected IpAddress ipAddress;
	private String pathError;
	protected DeviceController deviceController;
	private TelemetryConfiguration telemetryConfiguration = TelemetryConfiguration.of(this);

		
	protected RaspberryPIZero(String id, IpAddress ipAddress, String pathError) {
		this.itemId = id;
		this.ipAddress = ipAddress;
		this.pathError = pathError;
		
		telemetryConfiguration.addStrategy(new PlatformTelemetryStrategy(), 10000);
		telemetryConfiguration.addStrategy(new HardwareTelemetryStrategy(), 10000);
		telemetryConfiguration.addStrategy(new MemoryTelemetryStrategy(), 1000);
		telemetryConfiguration.addStrategy(new JavaTelemetryStrategy(), 10000);
		telemetryConfiguration.addStrategy(new NetworkTelemetryStrategy(), 5000);
		telemetryConfiguration.addStrategy(new SOTelemetryStrategy(), 10000);
		telemetryConfiguration.addStrategy(new ClockTelemetryStrategy(), 10000);
		telemetryConfiguration.addStrategy(new CodecTelemetryStrategy(), 10000);
		telemetryConfiguration.addStrategy(new TemperatureTelemetryStrategy(), 1000);
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

	@Override
	public String getPathError() {
		return pathError;
	}
	
	@Override
	public String[] getTopicsToPublishValue(CollectResult collectedData) {
		if (hasTelemetries()) {
			return new String[] {this.getPath()+"/systemInfo"};
		}
		return new String[] {};
	}

	@Override
	public TelemetryStrategy[] getTelemetries() {
		return telemetryConfiguration.getStrategies().keySet().toArray(new TelemetryStrategy[] {});
	}

	@Override
	public boolean hasTelemetries() {
		return telemetryConfiguration.hasTelemetries();
	}

	@Override
	public TelemetryStrategy[] getTelemetriesByInterval(long ellapsedTime) {
		List<TelemetryStrategy> result = new ArrayList<>();
		for (TelemetryStrategy strategy : telemetryConfiguration.getStrategies().keySet()) {
			if (ellapsedTime - strategy.getLastIntervalPublishing() >= telemetryConfiguration.getStrategies().get(strategy)) {
				result.add(strategy);
			}
		}
		return result.toArray(new TelemetryStrategy[] {});
	}
}
