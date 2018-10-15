package br.com.anteros.iot.controllers.remote;



import org.eclipse.paho.client.mqttv3.MqttClient;

import br.com.anteros.iot.MasterDeviceController;
import br.com.anteros.iot.RemoteMasterDeviceController;
import br.com.anteros.iot.RemoteSlaveDeviceController;
import br.com.anteros.iot.domain.DeviceMasterNode;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.domain.devices.MasterDeviceRPiNode;
import br.com.anteros.iot.domain.devices.SlaveRPiNode;
import br.com.anteros.iot.plant.Plant;

public class RemoteDeviceControllerFactory {
	
	public static RemoteSlaveDeviceController createSlaveFrom(MqttClient clientMqtt, MasterDeviceController master, final DeviceNode node, Plant plant) {
		if (node.getClass() == SlaveRPiNode.class)
			return RemoteSlaveControllerRpi.of(clientMqtt,node, master, plant);
		return null;
	}
	
	public static RemoteMasterDeviceController createMasterFrom(MqttClient clientMqtt, final DeviceMasterNode node, Plant plant) {
		if (node.getClass() == MasterDeviceRPiNode.class)
			return RemoteMasterControllerRPi.of(clientMqtt,node, plant);
		return null;
	}


}
