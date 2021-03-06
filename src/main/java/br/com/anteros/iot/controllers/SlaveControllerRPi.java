package br.com.anteros.iot.controllers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.client.mqttv3.IMqttDeliveryToken;
import br.com.anteros.client.mqttv3.MqttException;
import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.MasterDeviceController;
import br.com.anteros.iot.RemoteMasterDeviceController;
import br.com.anteros.iot.SlaveDeviceController;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.plant.Plant;
import br.com.anteros.iot.support.AnterosMqttClient;
import br.com.anteros.iot.things.devices.Computer;
import br.com.anteros.iot.things.devices.IpAddress;

public class SlaveControllerRPi extends AbstractDeviceController implements SlaveDeviceController {

	protected MasterDeviceController master;
	
	private static final Logger LOG = LoggerProvider.getInstance().getLogger(SlaveControllerRPi.class.getName());

	public SlaveControllerRPi(Device device, Actuators actuators) {
		super(device, actuators);
	}

	public SlaveControllerRPi(AnterosMqttClient remoteClientMqtt, DeviceNode node, MasterDeviceController master, Plant plant,
			Actuators actuators, AnterosIOTServiceListener serviceListener, String username, String password) {
		super(remoteClientMqtt, node, actuators, serviceListener, username, password);
		this.master = master;
		loadConfiguration(node, plant);
	}

	public SlaveControllerRPi(MasterDeviceController master, Device device, Actuators actuators) {
		super(device, actuators);
		this.master = master;
	}

	public SlaveControllerRPi(AnterosMqttClient clientMqtt, MasterDeviceController master, Device device, Actuators actuators,
			String username, String password) {
		super(clientMqtt, device, actuators, username, password);
		this.master = master;
	}

	public MasterDeviceController getMaster() {
		return master;
	}

	public Set<DeviceController> controllers() {
		return Collections.unmodifiableSet(new HashSet<DeviceController>());
	}

	public void connectionLost(Throwable cause) {
		LOG.info("Connection lost on instance \"" + getThingID() + "\" with cause \"" + cause.getMessage()
				+ "\" Reason code " + ((MqttException) cause).getReasonCode() + "\" Cause \""
				+ ((MqttException) cause).getCause() + "\"");
		cause.printStackTrace();
		
		try {
			this.clientMqtt.reconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			LOG.debug(
					"Delivery token \"" + token.hashCode() + "\" received by instance \"" + getThingID() + "\"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class Builder {

		private Device device;
		private Actuators actuators;
		private AnterosMqttClient clientMqtt;
		private RemoteMasterDeviceController master;
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

		public Builder master(RemoteMasterDeviceController master) {
			this.master = master;
			return this;
		}

		public SlaveDeviceController build() {
			Assert.notNull(device, "Informe o dispositivo para o controlador slave.");
			Assert.notNull(master, "Informe o dispositivo master remoto para o controlador slave.");
			Assert.notNull(clientMqtt, "Informe o dispositivo master remoto para o controlador slave.");
			return SlaveControllerRPi.of(clientMqtt, master, device, actuators, username, password);
		}

		public Builder clientMqtt(AnterosMqttClient clientMqtt) {
			this.clientMqtt = clientMqtt;
			return this;
		}

	}

	public static SlaveControllerRPi of(AnterosMqttClient clientMqtt, DeviceNode itemNode, MasterDeviceController master,
			Plant plant, Actuators actuators, AnterosIOTServiceListener serviceListener, String username,
			String password) {
		return new SlaveControllerRPi(clientMqtt, itemNode, master, plant, actuators, serviceListener, username,
				password);
	}

	public static SlaveControllerRPi of(AnterosMqttClient clientMqtt, MasterDeviceController master, Device device,
			Actuators actuators, String username, String password) {
		return new SlaveControllerRPi(clientMqtt, master, device, actuators, username, password);
	}

	@Override
	protected Device doCreateDevice(String deviceName, IpAddress ipAddress, String description, String topicError, Integer intervalPublishingTelemetry, String hostnameACL) {
		return Computer.of(deviceName, ipAddress, description, topicError, this, intervalPublishingTelemetry, hostnameACL);
	}

	@Override
	public void setMaster(MasterDeviceController master) {
		this.master = master;
	}

	@Override
	public String toString() {
		return "SlaveControllerComputer [ device=" + device + "]";
	}

}
