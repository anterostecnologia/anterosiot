package br.com.anteros.iot.things.devices;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.things.devices.telemetry.TelemetryStrategy;
import br.com.anteros.iot.triggers.Trigger;

public class BeagleBone extends PlantItem implements Device {
	
	private String hostnameACL;
	private IpAddress ipAddress;
	protected DeviceController deviceController;
	protected boolean needsPropagation;
	private String topicError;
	private Integer intervalPublishingTelemetry;
	private String primarySSID;
	private String primaryPassword;
	private String secondarySSID;
	private String secondaryPassword;
	protected Object userData;
	protected String status;
	protected String lastValue;
	
	public BeagleBone(String id, IpAddress ipAddress) {
		this.itemId = id;
		this.ipAddress = ipAddress;
	}

	public String getThingID() {
		return itemId;
	}

	public String getStatus() {
		return null;
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
	public String getTopicError() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TelemetryStrategy[] getTelemetries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasTelemetries() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TelemetryStrategy[] getTelemetriesByInterval(long ellapsedTime) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getIntervalPublishingTelemetry() {
		return intervalPublishingTelemetry;
	}

	public void setIntervalPublishingTelemetry(Integer intervalPublishingTelemetry) {
		this.intervalPublishingTelemetry = intervalPublishingTelemetry;
	}
	public void setTopicError(String topicError) {
		this.topicError = topicError;
	}

	
	@Override
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}

	public boolean isNeedsPropagation() {
		return needsPropagation;
	}

	public void setNeedsPropagation(boolean needsPropagation) {
		this.needsPropagation = needsPropagation;
	}

	public String getPrimarySSID() {
		return primarySSID;
	}

	public void setPrimarySSID(String primarySSID) {
		this.primarySSID = primarySSID;
	}

	public String getPrimaryPassword() {
		return primaryPassword;
	}

	public void setPrimaryPassword(String primaryPassword) {
		this.primaryPassword = primaryPassword;
	}

	public String getSecondarySSID() {
		return secondarySSID;
	}

	public void setSecondarySSID(String secondarySSID) {
		this.secondarySSID = secondarySSID;
	}

	public String getSecondaryPassword() {
		return secondaryPassword;
	}

	public void setSecondaryPassword(String secondaryPassword) {
		this.secondaryPassword = secondaryPassword;
	}

	public String getHostnameACL() {
		return hostnameACL;
	}

	public void setHostnameACL(String hostnameACL) {
		this.hostnameACL = hostnameACL;
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
		return DomainConstants.MASTER_BEAGLE_BONE;
	}

}
