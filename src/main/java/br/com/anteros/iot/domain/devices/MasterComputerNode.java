package br.com.anteros.iot.domain.devices;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.controllers.MasterControllerComputer;
import br.com.anteros.iot.domain.DeviceMasterNode;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.plant.Plant;

@JsonTypeName(DomainConstants.MASTER_COMPUTER)
public class MasterComputerNode extends DeviceMasterNode {

	public MasterComputerNode() {
		super();
	}

	public MasterComputerNode(String itemName, String description) {
		super(itemName, description);
	}

	@Override
	public AbstractDeviceController getInstanceOfDeviceController(MqttClient clientMqtt, Plant plant,
			Actuators actuators, AnterosIOTServiceListener serviceListener, String username, String password) {
		return MasterControllerComputer.of(clientMqtt, this, plant, actuators, serviceListener, username, password);
	}

	@Override
	public Thing getInstanceOfThing() {
		return null;
	}

}
