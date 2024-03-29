package br.com.anteros.iot.things.devices;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.things.devices.telemetry.TelemetryStrategy;
import br.com.anteros.iot.triggers.Trigger;

public class Computer extends PlantItem implements Device {
	
	private String hostnameACL;
	private IpAddress ipAddress;
	private String topicError;
	private Integer intervalPublishingTelemetry;
	protected String status;
	protected String lastValue;
	protected DeviceController deviceController;
	protected boolean needsPropagation;
	private Object userData;

	private Computer(String id, IpAddress ipAddress, String description, String topicError, AbstractDeviceController controller, Integer intervalPublishingTelemetry, String hostnameACL) {
		this.itemId = id;
		this.ipAddress = ipAddress;
		this.description = description;
		this.topicError = topicError == null ? "": topicError;
		this.deviceController = controller;
		this.intervalPublishingTelemetry = intervalPublishingTelemetry;
		this.hostnameACL = hostnameACL;
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
	
	public String getTopicError() {
		if ("".equals(topicError)) {
			return this.getPath() + "/error";
		}
		return topicError;
	}

	public static Device of(String deviceName, IpAddress ipAddress, String description, String topicError, AbstractDeviceController controller, Integer intervalPublishingTelemetry, String hostnameACL) {
		return new Computer(deviceName, ipAddress, description, topicError, controller, intervalPublishingTelemetry, hostnameACL);
	}

	public void setTopicError(String topicError) {
		this.topicError = topicError;
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
		return null;
	}

	public Integer getIntervalPublishingTelemetry() {
		return intervalPublishingTelemetry;
	}

	public void setIntervalPublishingTelemetry(Integer intervalPublishingTelemetry) {
		this.intervalPublishingTelemetry = intervalPublishingTelemetry;
	}

	@Override
	public String toString() {
		return "Computer [ipAddress=" + ipAddress + ", topicError=" + topicError + ", intervalPublishingTelemetry="
				+ intervalPublishingTelemetry + ", itemId=" + itemId + ", description=" + description + "]";
	}
	
	@Override
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}

	public String getHostnameACL() {
		return hostnameACL;
	}

	public void setHostnameACL(String hostnameACL) {
		this.hostnameACL = hostnameACL;
	}

	public boolean isNeedsPropagation() {
		return needsPropagation;
	}

	public void setNeedsPropagation(boolean needsPropagation) {
		this.needsPropagation = needsPropagation;
	}

	@Override
	public Boolean showLog() {
		return Boolean.FALSE;
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
		return DomainConstants.MASTER_COMPUTER;
	}

}
