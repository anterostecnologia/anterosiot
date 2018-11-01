package br.com.anteros.iot.domain;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.plant.Plant;
import br.com.anteros.iot.things.devices.IpAddress;

public abstract class DeviceNode extends PlantItemNode {
	
	protected IpAddress ipAddress;
	protected String pathError;
	
	@JsonIdentityInfo(
			  generator = ObjectIdGenerators.UUIDGenerator.class,
			  property = "@id")
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
	
	public abstract AbstractDeviceController getInstanceOfDeviceController(MqttClient clientMqtt, Plant currentPlant, Actuators actuators, AnterosIOTServiceListener serviceListener);

	public Set<ThingNode> getThings() {
		return things;
	}

	public void setThings(Set<ThingNode> things) {
		this.things = things;
	}

	public String getPathError() {
		return pathError;
	}

	public void setPathError(String pathError) {
		this.pathError = pathError;
	}


}
