package br.com.anteros.iot.controllers;

import java.util.HashSet;
import java.util.Set;

import br.com.anteros.client.mqttv3.IMqttDeliveryToken;
import br.com.anteros.client.mqttv3.MqttAsyncClient;
import br.com.anteros.client.mqttv3.MqttClient;
import br.com.anteros.client.mqttv3.MqttException;

import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.MasterDeviceController;
import br.com.anteros.iot.SlaveDeviceController;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.plant.Plant;
import br.com.anteros.iot.support.MqttHelper;
import br.com.anteros.iot.things.devices.Computer;
import br.com.anteros.iot.things.devices.IpAddress;


public class MasterControllerComputer extends AbstractDeviceController implements MasterDeviceController {

	protected Set<DeviceController> devices = new HashSet<DeviceController>();
	
	private static final Logger LOG = LoggerProvider.getInstance().getLogger(MasterControllerComputer.class.getName());

	protected MasterControllerComputer(Device device, Actuators actuators) {
		super(device, actuators);
	}

	protected MasterControllerComputer(MqttAsyncClient clientMqtt, Device device, Actuators actuators, String username, String password) {
		super(clientMqtt, device, actuators, username, password);
	}

	protected MasterControllerComputer(MqttAsyncClient clientMqtt, Device device, Actuators actuators,
			Set<DeviceController> slaves, String username, String password) {
		super(clientMqtt, device, actuators, username, password);
		this.devices.addAll(slaves);
	}

	public MasterControllerComputer(MqttAsyncClient clientMqtt, DeviceNode node, Plant plant, Actuators actuators,
			AnterosIOTServiceListener serviceListener, String username, String password) {
		super(clientMqtt, node, actuators, serviceListener, username, password);
		loadConfiguration(node, plant);
	}

	public MasterControllerComputer addChildDeviceController(DeviceController child) {
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
		private MqttAsyncClient clientMqtt;
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
			return MasterControllerComputer.of(clientMqtt, device, actuators, slaves, username, password);
		}

		public Builder clientMqtt(MqttAsyncClient clientMqtt) {
			this.clientMqtt = clientMqtt;
			return this;
		}

	}

	public static MasterDeviceController of(MqttAsyncClient clientMqtt, Device device, Actuators actuators,
			Set<DeviceController> slaves, String username, String password) {
		return new MasterControllerComputer(clientMqtt, device, actuators, slaves, username, password);
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
		LOG.info("Connection lost on instance \"" + getThingID() + "\" with cause \"" + cause.getMessage()
				+ "\" Reason code " + ((MqttException) cause).getReasonCode() + "\" Cause \""
				+ ((MqttException) cause).getCause() + "\"");
		cause.printStackTrace();
		
//		try {
//			clientMqtt = MqttHelper.createAndConnectMqttClient(clientMqtt.getServerURI(),
//					device.getThingID() + "_controller", username, password, true, true);
//		} catch (MqttException e1) {
//			e1.printStackTrace();
//		}
//
//		this.clientMqtt.setCallback(this);
//		this.autoSubscribe();
		
//		device.getDeviceController().stop();

//		try {
//			clientMqtt = MqttHelper.createAndConnectMqttClient(clientMqtt.getServerURI(),
//					device.getThingID() + "_controller", username, password, true, true);
//		} catch (MqttException e1) {
//			e1.printStackTrace();
//		}
//		
//		this.clientMqtt.setCallback(this);
//		
//		device.getDeviceController().start();
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			LOG.info(
					"Delivery token \"" + token.hashCode() + "\" received by instance \"" + getThingID() + "\"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadConfiguration(DeviceNode itemNode, Plant plant) {
		super.loadConfiguration(itemNode, plant);
	}

	public static MasterControllerComputer of(MqttAsyncClient clientMqtt, DeviceNode node, Plant plant, Actuators actuators,
			AnterosIOTServiceListener serviceListener, String username, String password) {
		return new MasterControllerComputer(clientMqtt, node, plant, actuators, serviceListener, username, password);
	}

	@Override
	protected Device doCreateDevice(String deviceName, IpAddress ipAddress, String description, String topicError, Integer intervalPublishingTelemetry, String hostnameACL) {
		return Computer.of(deviceName, ipAddress, description, topicError, this, intervalPublishingTelemetry, hostnameACL);
	}

	@Override
	public String toString() {
		return "MasterControllerComputer [ device=" + device + "]";
	}
}
