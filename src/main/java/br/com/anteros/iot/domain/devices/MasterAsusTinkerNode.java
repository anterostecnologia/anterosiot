package br.com.anteros.iot.domain.devices;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.domain.DeviceMasterNode;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.plant.Plant;

@JsonTypeName(DomainConstants.MASTER_ASUS_TINKER)
public class MasterAsusTinkerNode extends DeviceMasterNode {

	public MasterAsusTinkerNode() {
		super();
	}

	public MasterAsusTinkerNode(String itemName, String description) {
		super(itemName, description);
	}

	@Override
	public Thing getInstanceOfThing() {
		return null;
	}

	@Override
	public AbstractDeviceController getInstanceOfDeviceController(MqttClient clientMqtt, Plant plant, Actuators actuators, AnterosIOTServiceListener serviceListener) {
		return null;
	}
}
