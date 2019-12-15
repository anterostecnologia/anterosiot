package br.com.anteros.iot.domain;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.plant.Plant;
import br.com.anteros.iot.things.devices.IpAddress;

public abstract class DeviceNode extends PlantItemNode implements Configurable {

	protected IpAddress ipAddress;
	protected String topicError;
	protected boolean publishSystemInfo;
	protected boolean needsPropagation;
	protected Integer intervalPublishingTelemetry;
	protected String hostNtp;
	protected int timezoneNtp;	
	protected String hostMqtt;
	protected int portMqtt;
	protected String userMqtt;
	protected String passwordMqtt;

	@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@id")
	protected Set<ThingNode> things = new HashSet<>();

	public DeviceNode() {
		super();
	}

	public DeviceNode(String itemName, String description) {
		super(itemName, description);
	}

	public IpAddress getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(IpAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void addThingsToController(ThingNode... things) {
		for (ThingNode thing : things) {
			thing.setControllerOwner(this);
			this.things.add(thing);
		}
	}
	
	public abstract String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException;

	public abstract AbstractDeviceController getInstanceOfDeviceController(MqttAsyncClient clientMqtt, Plant currentPlant,
			Actuators actuators, AnterosIOTServiceListener serviceListener, String username, String password);

	public Set<ThingNode> getThings() {
		return things;
	}

	public void setThings(Set<ThingNode> things) {
		this.things = things;
	}

	public String getTopicError() {
		return topicError;
	}

	public void setTopicError(String topicError) {
		this.topicError = topicError;
	}

	public boolean isPublishSystemInfo() {
		return publishSystemInfo;
	}

	public void setPublishSystemInfo(boolean publishSystemInfo) {
		this.publishSystemInfo = publishSystemInfo;
	}

	public Integer getIntervalPublishingTelemetry() {
		return intervalPublishingTelemetry;
	}

	public void setIntervalPublishingTelemetry(Integer intervalPublishingTelemetry) {
		this.intervalPublishingTelemetry = intervalPublishingTelemetry;
	}
	public void setNeedsPropagation(boolean needsPropagation) {
		this.needsPropagation = needsPropagation;
	}
	
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}

	public String getHostMqtt() {
		return hostMqtt;
	}

	public void setHostMqtt(String hostMqtt) {
		this.hostMqtt = hostMqtt;
	}

	public int getPortMqtt() {
		return portMqtt;
	}

	public void setPortMqtt(int portMqtt) {
		this.portMqtt = portMqtt;
	}

	public String getUserMqtt() {
		return userMqtt;
	}

	public void setUserMqtt(String userMqtt) {
		this.userMqtt = userMqtt;
	}

	public String getPasswordMqtt() {
		return passwordMqtt;
	}

	public void setPasswordMqtt(String passwordMqtt) {
		this.passwordMqtt = passwordMqtt;
	}

	public String getHostNtp() {
		return hostNtp;
	}

	public void setHostNtp(String hostNtp) {
		this.hostNtp = hostNtp;
	}

	public int getTimezoneNtp() {
		return timezoneNtp;
	}

	public void setTimezoneNtp(int timezoneNtp) {
		this.timezoneNtp = timezoneNtp;
	}

	public boolean isNeedsPropagation() {
		return needsPropagation;
	}


}
