package br.com.anteros.iot.domain.devices;

import br.com.anteros.client.mqttv3.MqttAsyncClient;
import br.com.anteros.client.mqttv3.MqttClient;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.controllers.MasterControllerRPi;
import br.com.anteros.iot.domain.DeviceMasterNode;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
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
	public AbstractDeviceController getInstanceOfDeviceController(MqttAsyncClient clientMqtt, Plant plant,
			Actuators actuators, AnterosIOTServiceListener serviceListener, String username, String password) {
		return MasterControllerRPi.of(clientMqtt, this, plant, actuators, serviceListener, username, password);
	}
	
	@Override
	public String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException {
		return mapper.writeValueAsString(node);
	}
}
