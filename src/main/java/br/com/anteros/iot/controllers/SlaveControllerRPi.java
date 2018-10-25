package br.com.anteros.iot.controllers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Actuator;
import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.MasterDeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.RemoteMasterDeviceController;
import br.com.anteros.iot.SlaveDeviceController;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.plant.Plant;
import br.com.anteros.iot.protocol.IOTMessage;
import br.com.anteros.iot.things.devices.IpAddress;
import br.com.anteros.iot.things.devices.RaspberryPI;

public class SlaveControllerRPi extends AbstractDeviceController implements SlaveDeviceController {

	protected MasterDeviceController master;

	public SlaveControllerRPi(Device device, Actuators actuators) {
		super(device,actuators);
	}

	public SlaveControllerRPi(MqttClient clientMqtt, DeviceNode node, MasterDeviceController master, Plant plant, Actuators actuators, AnterosIOTServiceListener serviceListener) {
		super(clientMqtt, node, actuators, serviceListener);
		this.master = master;
		loadConfiguration(node, plant);
	}

	public SlaveControllerRPi(MasterDeviceController master, Device device,Actuators actuators) {
		super(device,actuators);
		this.master = master;
	}

	public SlaveControllerRPi(MqttClient clientMqtt, MasterDeviceController master, Device device, Actuators actuators) {
		super(clientMqtt, device, actuators);
		this.master = master;
	}

	public MasterDeviceController getMaster() {
		return master;
	}

	public Set<DeviceController> controllers() {
		return Collections.unmodifiableSet(new HashSet<DeviceController>());
	}

	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost on instance \"" + getThingID() + "\" with cause \"" + cause.getMessage()
				+ "\" Reason code " + ((MqttException) cause).getReasonCode() + "\" Cause \""
				+ ((MqttException) cause).getCause() + "\"");
		cause.printStackTrace();
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		try {
			System.out.println("=> Mensagem recebida: \"" + message.toString() + "\" no tópico \"" + topic.toString()
					+ "\" para instancia \"" + getThingID() + "\"");
		} catch (Exception e) {
			e.printStackTrace();
		}

		byte[] payload = message.getPayload();
		IOTMessage iotMessage = mapper.readValue(payload, IOTMessage.class);
		System.out.println(iotMessage);
		Thing thing = this.getThingByTopic(topic);
		System.out.println(thing);
		if (thing != null) {
			Actuator<?> actuator = actuators.discoverActuatorToThing(thing);
			System.out.println(actuator);
			if (actuator != null) {
//				Part part = thing.getPartById(iotMessage.getPart());
//				if (part != null) {
//					actuator.executeAction(iotMessage.getAction(), (part != null ? part : thing));
//				} else {
					actuator.executeAction(iotMessage.getAction(), thing);
				//}
			}
		}
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			System.out.println(
					"Delivery token \"" + token.hashCode() + "\" received by instance \"" + getThingID() + "\"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class Builder {

		private Device device;
		private Actuators actuators;
		private MqttClient clientMqtt;
		private RemoteMasterDeviceController master;

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
			return SlaveControllerRPi.of(clientMqtt, master, device, actuators);
		}

		public Builder clientMqtt(MqttClient clientMqtt) {
			this.clientMqtt = clientMqtt;
			return this;
		}

	}

	public static SlaveControllerRPi of(MqttClient clientMqtt, DeviceNode itemNode, MasterDeviceController master,
			Plant plant, Actuators actuators, AnterosIOTServiceListener serviceListener) {
		return new SlaveControllerRPi(clientMqtt, itemNode, master, plant, actuators, serviceListener);
	}

	public static SlaveControllerRPi of(MqttClient clientMqtt, MasterDeviceController master, Device device,
			Actuators actuators) {
		return new SlaveControllerRPi(clientMqtt, master, device, actuators);
	}

	@Override
	protected Device doCreateDevice(String deviceName, IpAddress ipAddress, String description) {
		return RaspberryPI.of(deviceName, ipAddress, description);
	}

	@Override
	public void setMaster(MasterDeviceController master) {
		this.master = master;
	}

	@Override
	public String toString() {
		return "SlaveControllerRPi [ device=" + device + "]";
	}

}
