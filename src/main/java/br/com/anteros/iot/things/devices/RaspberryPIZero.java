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
import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
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

public class RaspberryPIZero extends PlantItem implements Device, Publishable {

	private String hostnameACL;
	protected IpAddress ipAddress;
	private String topicError;
	private String primarySSID;
	private String primaryPassword;
	private String secondarySSID;
	private String secondaryPassword;
	private Integer intervalPublishingTelemetry;
	protected String status;
	protected String lastValue;
	protected DeviceController deviceController;
	protected boolean needsPropagation;

	private TelemetryConfiguration telemetryConfiguration = TelemetryConfiguration.of(this);
	private Object userData;

	protected RaspberryPIZero(String id, IpAddress ipAddress, String topicError, Integer intervalPublishingTelemetry) {
		this.itemId = id;
		this.ipAddress = ipAddress;
		this.topicError = topicError;

		telemetryConfiguration.addStrategy(new PlatformTelemetryStrategy(), intervalPublishingTelemetry);
		telemetryConfiguration.addStrategy(new HardwareTelemetryStrategy(), intervalPublishingTelemetry);
		telemetryConfiguration.addStrategy(new MemoryTelemetryStrategy(), intervalPublishingTelemetry);
		telemetryConfiguration.addStrategy(new JavaTelemetryStrategy(), intervalPublishingTelemetry);
		telemetryConfiguration.addStrategy(new NetworkTelemetryStrategy(), intervalPublishingTelemetry);
		telemetryConfiguration.addStrategy(new SOTelemetryStrategy(), intervalPublishingTelemetry);
		telemetryConfiguration.addStrategy(new ClockTelemetryStrategy(), intervalPublishingTelemetry);
		telemetryConfiguration.addStrategy(new CodecTelemetryStrategy(), intervalPublishingTelemetry);
		telemetryConfiguration.addStrategy(new TemperatureTelemetryStrategy(), intervalPublishingTelemetry);
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
		return null;
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		return null;
	}

	@Override
	public Device setIpAddress(IpAddress ipAddress) {
		this.ipAddress = ipAddress;
		return this;
	}

	@Override
	protected boolean acceptThisTypeOfPlantItem(Class<?> child) {
		return ReflectionUtils.isImplementsInterface(child, Thing.class)
				&& !ReflectionUtils.isImplementsInterface(child, Part.class)
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
		return null;
	}

	@Override
	public Thing addTrigger(Trigger trigger) {
		return null;
	}

	@Override
	public Thing removeTrigger(Trigger trigger) {
		return null;
	}

	@Override
	public Trigger[] getTriggers() {
		return null;
	}

	@Override
	public String getTopicError() {
		return topicError;
	}

	@Override
	public String[] getTopicsToPublishValue(CollectResult collectedData) {
		if (hasTelemetries()) {
			return new String[] { this.getPath() + "/systemInfo" };
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
			if (ellapsedTime - strategy.getLastIntervalPublishing() >= telemetryConfiguration.getStrategies()
					.get(strategy)) {
				result.add(strategy);
			}
		}
		return result.toArray(new TelemetryStrategy[] {});
	}

	public Integer getIntervalPublishingTelemetry() {
		return intervalPublishingTelemetry;
	}

	public void setIntervalPublishingTelemetry(Integer intervalPublishingTelemetry) {
		this.intervalPublishingTelemetry = intervalPublishingTelemetry;
	}

	public TelemetryConfiguration getTelemetryConfiguration() {
		return telemetryConfiguration;
	}

	public void setTelemetryConfiguration(TelemetryConfiguration telemetryConfiguration) {
		this.telemetryConfiguration = telemetryConfiguration;
	}

	public void setTopicError(String topicError) {
		this.topicError = topicError;
	}

	@Override
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
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

	public boolean isNeedsPropagation() {
		return needsPropagation;
	}

	public void setNeedsPropagation(boolean needsPropagation) {
		this.needsPropagation = needsPropagation;
	}

	@Override
	public String toString() {
		return "RaspberryPIZero [ipAddress=" + ipAddress + ", topicError=" + topicError + ", primarySSID=" + primarySSID
				+ ", primaryPassword=" + primaryPassword + ", secondarySSID=" + secondarySSID + ", secondaryPassword="
				+ secondaryPassword + ", intervalPublishingTelemetry=" + intervalPublishingTelemetry
				+ ", deviceController=" + deviceController + ", needsPropagation=" + needsPropagation
				+ ", telemetryConfiguration=" + telemetryConfiguration + "]";
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
		return DomainConstants.MASTER_RPI_ZERO;
	}

}
