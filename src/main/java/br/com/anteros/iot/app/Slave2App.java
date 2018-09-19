package br.com.anteros.iot.app;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import br.com.anteros.iot.Device;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.RemoteMasterDeviceController;
import br.com.anteros.iot.SlaveDeviceController;
import br.com.anteros.iot.actuators.LedActuator;
import br.com.anteros.iot.controllers.SlaveControllerRPi;
import br.com.anteros.iot.controllers.remote.RemoteMasterControllerRPi;
import br.com.anteros.iot.things.Semaphore;
import br.com.anteros.iot.things.devices.IpAddress;
import br.com.anteros.iot.things.devices.RaspberryPI;
import br.com.anteros.iot.things.parts.GreenLEDSemaphorePart;
import br.com.anteros.iot.things.parts.RedLEDSemaphorePart;
import br.com.anteros.iot.things.parts.YellowLEDSemaphorePart;

public class Slave2App {

	public static void main(String[] args) {

		String topic = "Teste";
		String broker = "tcp://10.0.0.152:1883";
		String clientId = "ServidorSlave2";
		MemoryPersistence persistence = new MemoryPersistence();

		try {
			MqttClient client = new MqttClient(broker, clientId, persistence);

			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setAutomaticReconnect(true);
			connOpts.setCleanSession(true);
			client.connect(connOpts);

			client.subscribe(new String[] {"Teste","LED"});

			/*
			 * 2 dispositivos
			 */
			Device masterDevice = RaspberryPI.of("ServidorMaster", IpAddress.of("10.0.0.152"));
			Device slave2Device = RaspberryPI.of("ServidorSlave2", IpAddress.of("10.0.0.154"));

			/*
			 * Um atuador
			 */
			LedActuator ledActuator = new LedActuator();

			/*
			 * Um controlador master remoto
			 */
			RemoteMasterDeviceController remoteMaster = RemoteMasterControllerRPi.Builder.create()
					.device(masterDevice).build();
			/*
			 * Controlador slave 2
			 */
			SlaveDeviceController slave2 = SlaveControllerRPi.Builder.create().device(slave2Device).clientMqtt(client)
					.master(remoteMaster).build();

			/*
			 * Uma coisa
			 */
			Semaphore semaphore = new Semaphore("Semaforo");
			Part redLed = RedLEDSemaphorePart.of("Led Red", semaphore, Semaphore.GPIO_01);
			Part greenLed = GreenLEDSemaphorePart.of("Led Green", semaphore, Semaphore.GPIO_02);
			Part yellowLed = YellowLEDSemaphorePart.of("Led Yellow", semaphore, Semaphore.GPIO_03);
			semaphore.addPart(redLed);
			semaphore.addPart(greenLed);
			semaphore.addPart(yellowLed);

			/*
			 * Inicia o master
			 */
			slave2.addThings(semaphore);
			slave2.registerActuator(ledActuator);
			slave2.start();

		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

}
