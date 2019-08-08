package br.com.anteros.iot.domain.devices;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.controllers.SlaveControllerComputer;
import br.com.anteros.iot.domain.DeviceSlaveNode;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.plant.Plant;

@JsonTypeName(DomainConstants.SLAVE_COMPUTER)
public class SlaveComputerNode extends DeviceSlaveNode {

	public SlaveComputerNode() {
		super();
	}

	public SlaveComputerNode(String itemName, String description) {
		super(itemName, description);
	}

	@Override
	public Thing getInstanceOfThing() {
		return null;
	}

	@Override
	public AbstractDeviceController getInstanceOfDeviceController(MqttAsyncClient clientMqtt, Plant plant,
			Actuators actuators, AnterosIOTServiceListener serviceListener, String username, String password) {
		return SlaveControllerComputer.of(clientMqtt, this, null, plant, actuators, serviceListener, username,
				password);
	}

}
