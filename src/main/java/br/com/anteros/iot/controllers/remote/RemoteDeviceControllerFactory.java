package br.com.anteros.iot.controllers.remote;




import br.com.anteros.iot.MasterDeviceController;
import br.com.anteros.iot.RemoteMasterDeviceController;
import br.com.anteros.iot.RemoteSlaveDeviceController;
import br.com.anteros.iot.domain.DeviceMasterNode;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.domain.devices.MasterDeviceRPiNode;
import br.com.anteros.iot.domain.devices.SlaveRPiNode;
import br.com.anteros.iot.plant.Plant;
import br.com.anteros.iot.support.AnterosMqttClient;

public class RemoteDeviceControllerFactory {

	public static RemoteSlaveDeviceController createSlaveFrom(AnterosMqttClient remoteClientMqtt, MasterDeviceController master,
			final DeviceNode node, Plant plant, String username, String password) {
		if (node.getClass() == SlaveRPiNode.class)
			return RemoteSlaveControllerRpi.of(remoteClientMqtt, node, master, plant, username, password);
		return null;
	}

	public static RemoteMasterDeviceController createMasterFrom(AnterosMqttClient remoteClientMqtt, final DeviceMasterNode node,
			Plant plant, String username, String password) {
		if (node.getClass() == MasterDeviceRPiNode.class)
			return RemoteMasterControllerRPi.of(remoteClientMqtt, node, plant, username, password);
		return null;
	}

}
