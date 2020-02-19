package br.com.anteros.iot.controllers.remote;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.MasterDeviceController;
import br.com.anteros.iot.RemoteSlaveDeviceController;
import br.com.anteros.iot.controllers.DeviceException;
import br.com.anteros.iot.controllers.SlaveControllerRPi;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.plant.Place;
import br.com.anteros.iot.plant.Plant;
import br.com.anteros.iot.plant.PlantItem;

public class RemoteSlaveControllerRpi extends SlaveControllerRPi implements RemoteSlaveDeviceController {

	public RemoteSlaveControllerRpi(MasterDeviceController master, Device device) {
		super(master, device,null);
	}

	public RemoteSlaveControllerRpi(MqttAsyncClient remoteClientMqtt, DeviceNode node, MasterDeviceController master,
			Plant plant, String username, String password) {
		super(remoteClientMqtt, node, master, plant,null, null, username, password);
	}

	@Override
	public void stop() {
	}

	public void messagePublish() {

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {

	}

	public static class Builder {

		private Device device;
		private MasterDeviceController master;

		public static Builder create() {
			return new Builder();
		}

		public Builder device(Device device) {
			this.device = device;
			return this;
		}

		public Builder master(MasterDeviceController master) {
			this.master = master;
			return this;
		}

		public RemoteSlaveDeviceController build() {
			Assert.notNull(device, "Informe o dispositivo remoto para o controlador slave.");
			Assert.notNull(master, "Informe o dispositivo master para o controlador slave.");
			Assert.notNull(master, "Informe o dispositivo master para o controlador slave.");
			return RemoteSlaveControllerRpi.of(master, device);
		}
	}

	public static RemoteSlaveDeviceController of(MasterDeviceController master, Device device) {
		return new RemoteSlaveControllerRpi(master, device);
	}

	public static RemoteSlaveControllerRpi of(MqttAsyncClient remoteClientMqtt, DeviceNode node, MasterDeviceController master,
			Plant plant, String username, String password) {
		return new RemoteSlaveControllerRpi(remoteClientMqtt, node, master, plant, username, password);
	}

	@Override
	public void loadConfiguration(DeviceNode itemNode, Plant plant) {
		Place place = (Place) plant.getItemByName(itemNode.getItemNodeOwner().getItemName());
		this.device = doCreateDevice(itemNode.getItemName(), itemNode.getIpAddress(),itemNode.getDescription(), itemNode.getTopicError(), itemNode.getIntervalPublishingTelemetry(), itemNode.getHostnameACL());
		if (!(this.device instanceof PlantItem)) {
			throw new DeviceException("O device " + itemNode.getItemName() + " não é um item da planta.");
		}
		place.addItems((PlantItem) device);
	}

	@Override
	public String toString() {
		return "RemoteSlaveControllerRpi [master=" + master + ", actuators=" + actuators + ", device=" + device
				+ ", things=" + things + ", clientMqtt=" + clientMqtt + ", running=" + running + ", paused=" + paused
				+ ", thread=" + thread + ", mapper=" + mapper + "]";
	}
	
	

}
