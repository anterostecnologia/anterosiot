package br.com.anteros.iot.domain.devices;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.controllers.MasterControllerRPi;
import br.com.anteros.iot.domain.DeviceMasterNode;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.things.PresenceDetectorNode;
import br.com.anteros.iot.domain.things.TemperatureOneWireNode;
import br.com.anteros.iot.plant.Plant;

@JsonTypeName(DomainConstants.MASTER_RPI)
public class MasterDeviceRPiNode extends DeviceMasterNode {

	public MasterDeviceRPiNode() {
		super();
	}

	public MasterDeviceRPiNode(String itemName, String description) {
		super(itemName, description);
	}

	@Override
	public Thing getInstanceOfThing() {
		return null;
	}
	
	@Override
	public AbstractDeviceController getInstanceOfDeviceController(MqttClient clientMqtt, Plant plant, Actuators actuators) {
		return MasterControllerRPi.of(clientMqtt, this, plant, actuators);
	}

	

}
