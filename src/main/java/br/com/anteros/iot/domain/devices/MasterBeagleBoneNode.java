package br.com.anteros.iot.domain.devices;




import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.domain.DeviceMasterNode;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.plant.Plant;
import br.com.anteros.iot.support.AnterosMqttClient;

@JsonTypeName(DomainConstants.MASTER_BEAGLE_BONE)
public class MasterBeagleBoneNode extends DeviceMasterNode {

	public MasterBeagleBoneNode() {
		super();
	}

	public MasterBeagleBoneNode(String itemName, String description) {
		super(itemName, description);
	}

	@Override
	public Thing getInstanceOfThing() {
		return null;
	}

	@Override
	public AbstractDeviceController getInstanceOfDeviceController(AnterosMqttClient clientMqtt, Plant plant,
			Actuators actuators, AnterosIOTServiceListener serviceListener, String username, String password) {
		return null;
	}
	
	@Override
	public String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException {
		return mapper.writeValueAsString(node);
	}

}
