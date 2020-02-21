package br.com.anteros.iot.controllers.remote;

import br.com.anteros.client.mqttv3.MqttAsyncClient;
import br.com.anteros.client.mqttv3.MqttClient;
import br.com.anteros.client.mqttv3.MqttMessage;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.RemoteMasterDeviceController;
import br.com.anteros.iot.controllers.MasterControllerRPi;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.plant.Plant;

public class RemoteMasterControllerRPi extends MasterControllerRPi implements RemoteMasterDeviceController {

	public RemoteMasterControllerRPi(Device device) {
		super(device,null);
	}
	
	public RemoteMasterControllerRPi(MqttAsyncClient remoteClientMqtt,DeviceNode node, Plant plant, String username, String password) {
		super(remoteClientMqtt,node, plant,null, null, username, password);
	}
	
	
	public static class Builder {

		private Device device;

		public static Builder create() {
			return new Builder();
		}

		public Builder device(Device device) {
			this.device = device;
			return this;
		}
		
		public RemoteMasterDeviceController build() {
			Assert.notNull(device, "Informe o dispositivo para o controlador master remoto.");
			return RemoteMasterControllerRPi.of(device);
		}

	}
	
	@Override
	public void loadConfiguration(DeviceNode itemNode, Plant plant) {
		
	}


	public static RemoteMasterControllerRPi of(Device device) {
		return new RemoteMasterControllerRPi(device);
	}
	

	public static RemoteMasterControllerRPi of(MqttAsyncClient remoteClientMqtt, DeviceNode node, Plant plant, String username, String password) {
		return new RemoteMasterControllerRPi(remoteClientMqtt,node, plant, username, password);
	}
	
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
	}
	

}
