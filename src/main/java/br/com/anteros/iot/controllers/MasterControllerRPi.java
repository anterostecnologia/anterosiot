package br.com.anteros.iot.controllers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Actuator;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.DeviceStatus;
import br.com.anteros.iot.MasterDeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.SlaveDeviceController;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.protocol.IOTMessage;

public class MasterControllerRPi implements MasterDeviceController {

	protected Set<DeviceController> devices = new HashSet<DeviceController>();
	protected Set<Actuator> actuators = new HashSet<Actuator>();
	protected Device device;
	protected Set<Thing> things = new HashSet<Thing>();
	protected MqttClient clientMqtt;
	protected Boolean running = false;
	protected Thread thread;
	protected ObjectMapper mapper = new ObjectMapper();

	protected MasterControllerRPi(Device device) {
		this.device = device;
		this.thread = new Thread(this);
	}	
	protected MasterControllerRPi(MqttClient clientMqtt, Device device) {
		this(device);
		this.clientMqtt = clientMqtt;
		this.clientMqtt.setCallback(this);
	}

	protected MasterControllerRPi(MqttClient clientMqtt, Device device, Set<DeviceController> slaves) {
		this(clientMqtt, device);
		this.devices.addAll(slaves);
	}

	public MasterControllerRPi(MqttClient clientMqtt, Device device, Set<Actuator> actuators, Set<DeviceController> slaves) {
		this(clientMqtt, device, slaves);
		this.actuators.addAll(actuators);
	}

	public MasterControllerRPi addChildDeviceController(DeviceController child) {
		devices.add(child);
		return this;
	}

	public Set<DeviceController> controllers() {
		return devices;
	}

	public Actuator discoverActuatorToThing(Thing thing) {
		for (Actuator actuator : actuators) {
			if (actuator.isSupportedThing(thing)) {
				return actuator;
			}
		}
		return null;
	}

	public void registerActuator(Actuator actuator) {
		actuators.add(actuator);
	}

	public void unregisterActuator(Actuator actuator) {
		actuators.remove(actuator);
	}

	public Thing getThing() {
		return device;
	}

	public DeviceStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public void beforeStart() {
		// TODO Auto-generated method stub

	}

	public void beforeStop() {
		// TODO Auto-generated method stub

	}

	public void beforeRestart() {
		// TODO Auto-generated method stub

	}

	public void afterStart() {
		// TODO Auto-generated method stub

	}

	public void afterRestart() {
		// TODO Auto-generated method stub

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public void start() {
		this.thread.start();
	}

	public void pause() {
		this.thread.suspend();
	}

	public void resume() {
		this.thread.resume();

	}

	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getThingID() {
		return device.getThingID();
	}

	public static class Builder {

		private Device device;
		private Set<Actuator> actuators = new HashSet<Actuator>();
		private Set<DeviceController> slaves = new HashSet<DeviceController>();
		private MqttClient clientMqtt;

		public static Builder create() {
			return new Builder();
		}

		public Builder device(Device device) {
			this.device = device;
			return this;
		}

		public Builder atuactors(Actuator... actuators) {
			for (Actuator a : actuators) {
				this.actuators.add(a);
			}
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
			return MasterControllerRPi.of(clientMqtt, device, actuators, slaves);
		}

		public Builder clientMqtt(MqttClient clientMqtt) {
			this.clientMqtt = clientMqtt;
			return this;
		}

	}

	public static MasterDeviceController of(MqttClient clientMqtt, Device device, Set<Actuator> actuators, Set<DeviceController> slaves) {
		return new MasterControllerRPi(clientMqtt, device, actuators, slaves);
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

	public DeviceController addThings(Thing... things) {
		this.things.addAll(Arrays.asList(things));
		return this;
	}

	public DeviceController removeThing(Thing thing) {
		this.things.remove(thing);
		return this;
	}
	
	public Thing getThingById(String thingId) {
		for (Thing thing : things) {
			if (thing.getThingID().equals(thingId)) {
				return thing;
			}
		}
		return null;
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

		if (topic.equals("LED")) {
			byte[] payload = message.getPayload();
			IOTMessage iotMessage = mapper.readValue(payload, IOTMessage.class);
			if (iotMessage.getDeviceController().equals(this.getThingID())) {
				Thing thing = this.getThingById(iotMessage.getThing());
				if (thing != null) {
					Actuator actuator = this.discoverActuatorToThing(thing);
					if (actuator != null) {
						Part part = thing.getPartById(iotMessage.getPart());
						System.out.println("    Executando acão " + iotMessage.getAction() + " em " + thing.getThingID()
								+ part != null ? " parte " + part.getThingID() : "");
						if (actuator.executeAction(iotMessage.getAction(), (part != null ? part : thing))) {
							System.out.println("    Ação executada " + iotMessage.getAction() + " em " + thing.getThingID()
							+ part != null ? " parte " + part.getThingID() : "");
						}
					}
				}
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

	public void run() {
		this.running = true;
		System.out.println("Iniciando controlador master "+this.getThingID());
		boolean first = true;
		while (running) {
			if (first) {
				System.out.println("Servidor master "+this.getThingID()+" rodando.");
				first = false;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Parando controlador master "+this.getThingID());
		this.thread.interrupt();		
	}

}
