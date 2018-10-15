package br.com.anteros.iot.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Actuator;
import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.DefaultActuators;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.DeviceStatus;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.collectors.CameraQRCodeCollector;
import br.com.anteros.iot.collectors.CollectorManager;
import br.com.anteros.iot.collectors.PresenceDetectorCollector;
import br.com.anteros.iot.collectors.RFIDReaderCollector;
import br.com.anteros.iot.collectors.SimpleCollectorManager;
import br.com.anteros.iot.collectors.TemperatureOneWireCollector;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.plant.Place;
import br.com.anteros.iot.plant.Plant;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.support.MqttHelper;
import br.com.anteros.iot.things.Publishable;
import br.com.anteros.iot.things.devices.IpAddress;

public abstract class AbstractDeviceController implements DeviceController, MqttCallback, Runnable {

	protected Device device;
	protected Set<Thing> things = new HashSet<Thing>();
	protected MqttClient clientMqtt;
	protected Boolean running = false;
	protected Boolean paused = false;
	protected Thread thread;
	protected ObjectMapper mapper = new ObjectMapper();
	protected Actuators actuators;

	public AbstractDeviceController() {

	}

	public AbstractDeviceController(MqttClient clientMqtt, DeviceNode node, Actuators actuators) {
		this.clientMqtt = clientMqtt;
		this.clientMqtt.setCallback(this);
		this.thread = new Thread(this);
		this.actuators = actuators;
	}

	protected AbstractDeviceController(Device device, Actuators actuators) {
		this.device = device;
		this.thread = new Thread(this);
		this.actuators = actuators;
	}

	protected AbstractDeviceController(MqttClient clientMqtt, Device device, Actuators actuators) {
		this(device, actuators);
		this.clientMqtt = clientMqtt;
		this.clientMqtt.setCallback(this);
		this.actuators = actuators;
	}

	public Thing getThing() {
		return device;
	}

	public DeviceStatus getStatus() {
		return null;
	}

	public void beforeStart() {

	}

	public void beforeStop() {

	}

	public void beforeRestart() {

	}

	public void afterStart() {

	}

	public void afterRestart() {

	}

	@Override
	public void beforePause() {

	}

	@Override
	public void afterResume() {

	}

	public void stop() {
		if (running) {
			this.beforeStop();
			this.running = false;
		}
	}

	public void start() {
		if (!running) {
			this.beforeStart();
			this.thread.start();
			this.afterStart();
		}
	}

	public void pause() {
		if (!paused) {
			this.beforePause();
			this.paused = true;
		}
	}

	public void resume() {
		if (paused) {
			this.paused = false;
			this.afterResume();
		}
	}

	public boolean isAvailable() {
		return !paused && running;
	}

	public String getThingID() {
		return device.getThingID();
	}

	public DeviceController addThings(Thing... things) {
		for (Thing thing : things) {
			thing.setDeviceController(this);
		}
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

	public void run() {
		this.running = true;
		System.out.println("Iniciando controlador " + this.getThingID());

		MqttClient clientCollector = null;
		try {
			clientCollector = MqttHelper.createAndConnectMqttClient(clientMqtt.getServerURI(), this.device.getThingID() + "_collector", "", "", true, true);
		} catch (MqttException e1) {
			e1.printStackTrace();
		}

		CollectorManager collectorManager = SimpleCollectorManager.of(clientCollector, things.toArray(new Thing[] {}), new DefaultActuators());
		collectorManager.start();

		boolean first = true;
		while (running) {
			if (first) {
				System.out.println("Servidor master " + this.getThingID() + " rodando.");
				first = false;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		collectorManager.stop();
		System.out.println("Parando controlador " + this.getThingID());
		this.thread.interrupt();
	}

	@Override
	public void restartOS() {
		Process p;
		try {
			p = Runtime.getRuntime().exec("sudo shutdown -h now");
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public abstract void connectionLost(Throwable cause);	

	public abstract void messageArrived(String topic, MqttMessage message) throws Exception;

	public abstract void deliveryComplete(IMqttDeliveryToken token);

	protected abstract Device doCreateDevice(String deviceName, IpAddress ipAddress, String description);

	public void loadConfiguration(DeviceNode itemNode, Plant plant) {
		Place place = (Place) plant.getItemByName(itemNode.getItemNodeOwner().getItemName());
		this.device = doCreateDevice(itemNode.getItemName(), itemNode.getIpAddress(), itemNode.getDescription());
		System.out.println("aqui");
		System.out.println(device);
		if (!(this.device instanceof PlantItem)) {
			throw new DeviceException("O device " + itemNode.getItemName() + " não é um item da planta.");
		}
		place.addItems((PlantItem) device);
	}

	@Override
	public void autoSubscribe() {

		List<String> filter = new ArrayList<>();

		for (Thing thing : things) {
			if (thing instanceof PlantItem && !(thing instanceof Publishable)) {
				filter.add(((PlantItem) thing).getPath());
			}
		}

		if (this.getThing() instanceof PlantItem) {
			filter.add(((PlantItem) this.getThing()).getPath());
		}
		
		try {
			System.out.println(Arrays.toString(filter.toArray(new String[] {})));
			this.clientMqtt.subscribe(filter.toArray(new String[] {}));
		} catch (MqttException e) {
			e.printStackTrace();
		}
		
		try {
			this.clientMqtt.subscribe("empresa/#");
		} catch (MqttException e) {
			e.printStackTrace();
		}
		

	}

}
