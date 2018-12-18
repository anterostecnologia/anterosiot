package br.com.anteros.iot.controllers;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.MasterDeviceController;
import br.com.anteros.iot.SlaveDeviceController;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.plant.Plant;
import br.com.anteros.iot.things.devices.IpAddress;
import br.com.anteros.iot.things.devices.RaspberryPI;

public class MasterControllerRPi extends AbstractDeviceController implements MasterDeviceController {

	protected Set<DeviceController> devices = new HashSet<DeviceController>();

	protected MasterControllerRPi(Device device, Actuators actuators) {
		super(device, actuators);
	}

	protected MasterControllerRPi(MqttClient clientMqtt, Device device, Actuators actuators, String username, String password) {
		super(clientMqtt, device, actuators, username, password);
	}

	protected MasterControllerRPi(MqttClient clientMqtt, Device device, Actuators actuators,
			Set<DeviceController> slaves, String username, String password) {
		super(clientMqtt, device, actuators, username, password);
		this.devices.addAll(slaves);
	}

	public MasterControllerRPi(MqttClient clientMqtt, DeviceNode node, Plant plant, Actuators actuators,
			AnterosIOTServiceListener serviceListener, String username, String password) {
		super(clientMqtt, node, actuators, serviceListener, username, password);
		loadConfiguration(node, plant);
	}

	public MasterControllerRPi addChildDeviceController(DeviceController child) {
		devices.add(child);
		return this;
	}

	public Set<DeviceController> controllers() {
		return devices;
	}

	public static class Builder {

		private Device device;
		private Actuators actuators;
		private Set<DeviceController> slaves = new HashSet<>();
		private MqttClient clientMqtt;
		private String username; 
		private String password;

		public static Builder create() {
			return new Builder();
		}

		public Builder device(Device device) {
			this.device = device;
			return this;
		}

		public Builder actuators(Actuators actuators) {
			this.actuators = actuators;
			return this;
		}

		public Builder slaves(SlaveDeviceController... slaves) {
			for (SlaveDeviceController s : slaves) {
				this.slaves.add(s);
			}
			return this;
		}

		public MasterDeviceController build() {
			Assert.notNull(device, "Informe o dispositivo para o controlador master.");
			return MasterControllerRPi.of(clientMqtt, device, actuators, slaves, username, password);
		}

		public Builder clientMqtt(MqttClient clientMqtt) {
			this.clientMqtt = clientMqtt;
			return this;
		}

	}

	public static MasterDeviceController of(MqttClient clientMqtt, Device device, Actuators actuators,
			Set<DeviceController> slaves, String username, String password) {
		return new MasterControllerRPi(clientMqtt, device, actuators, slaves, username, password);
	}

	public void stopAllSlaves() {
		for (DeviceController controller : devices) {
			if (controller instanceof SlaveDeviceController) {
				controller.stop();
			}
		}

	}

	public MasterDeviceController removeChildDeviceController(DeviceController child) {
		this.devices.remove(child);
		return this;
	}

	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost on instance \"" + getThingID() + "\" with cause \"" + cause.getMessage()
				+ "\" Reason code " + ((MqttException) cause).getReasonCode() + "\" Cause \""
				+ ((MqttException) cause).getCause() + "\"");
		cause.printStackTrace();
	}	

	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			System.out.println(
					"Delivery token \"" + token.hashCode() + "\" received by instance \"" + getThingID() + "\"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadConfiguration(DeviceNode itemNode, Plant plant) {
		super.loadConfiguration(itemNode, plant);
	}

	public static MasterControllerRPi of(MqttClient clientMqtt, DeviceNode node, Plant plant, Actuators actuators,
			AnterosIOTServiceListener serviceListener, String username, String password) {
		return new MasterControllerRPi(clientMqtt, node, plant, actuators, serviceListener, username, password);
	}

	@Override
	protected Device doCreateDevice(String deviceName, IpAddress ipAddress, String description, String pathError) {
		return RaspberryPI.of(deviceName, ipAddress, description, pathError);
	}

	@Override
	public String toString() {
		return "MasterControllerRPi [ device=" + device + "]";
	}

}
