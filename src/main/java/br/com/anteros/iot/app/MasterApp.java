package br.com.anteros.iot.app;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import br.com.anteros.iot.Device;
import br.com.anteros.iot.MasterDeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.RemoteSlaveDeviceController;
import br.com.anteros.iot.actuators.LedActuator;
import br.com.anteros.iot.controllers.MasterControllerRPi;
import br.com.anteros.iot.controllers.remote.RemoteSlaveControllerRpi;
import br.com.anteros.iot.things.Semaphore;
import br.com.anteros.iot.things.devices.IpAddress;
import br.com.anteros.iot.things.devices.RaspberryPI;
import br.com.anteros.iot.things.parts.GreenLEDSemaphorePart;
import br.com.anteros.iot.things.parts.RedLEDSemaphorePart;
import br.com.anteros.iot.things.parts.YellowLEDSemaphorePart;

public class MasterApp  {

	public static void main(String[] args) {
		
		String broker = "tcp://10.0.0.152:1883";
		String clientId = "ServidorMaster";
		MemoryPersistence persistence = new MemoryPersistence();
		
		try {
			System.out.println("Conectando servidor broker MQTT...");
			MqttClient client = new MqttClient(broker, clientId, persistence);
			
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setAutomaticReconnect(true);
			connOpts.setCleanSession(true);
			client.connect(connOpts);
			System.out.println("Conectado servidor broker MQTT");
			
			client.subscribe(new String[] {"Teste","LED"});
			
			System.out.println("Subscribe em Teste,LED...");
			/*
			 * 3 dispositivos
			 */
			Device masterDevice = RaspberryPI.of("ServidorMaster",IpAddress.of("10.0.0.152"));
			Device slave1Device = RaspberryPI.of("ServidorSlave1",IpAddress.of("10.0.0.153"));
			Device slave2Device = RaspberryPI.of("ServidorSlave2",IpAddress.of("10.0.0.154"));

			/*
			 * Um atuador
			 */
			LedActuator ledActuator = new LedActuator();
			
			/*
			 * Um controlador master
			 */
			MasterDeviceController master = MasterControllerRPi.Builder.create().clientMqtt(client).device(masterDevice).atuactors(ledActuator)
					.build();
			/*
			 * Dois controladores slaves
			 */
			RemoteSlaveDeviceController slave1 = RemoteSlaveControllerRpi.Builder.create().device(slave1Device).master(master).build();
			RemoteSlaveDeviceController slave2 = RemoteSlaveControllerRpi.Builder.create().device(slave2Device).master(master).build();

			master.addChildDeviceController(slave1).addChildDeviceController(slave2);
			
			/*
			 * Uma coisa
			 */
			Semaphore semaphore = new Semaphore("Semaforo");
			Part redLed = RedLEDSemaphorePart.of("Led Red",semaphore, Semaphore.GPIO_01);
			Part greenLed = GreenLEDSemaphorePart.of("Led Green",semaphore,Semaphore.GPIO_02);
			Part yellowLed = YellowLEDSemaphorePart.of("Led Yellow",semaphore,Semaphore.GPIO_03);
			semaphore.addPart(redLed);
			semaphore.addPart(greenLed);
			semaphore.addPart(yellowLed);
			
			/*
			 * Inicia o master
			 */
			master.addThings(semaphore);
			master.registerActuator(ledActuator);
			master.start();
			
		} catch (MqttException e) {
			e.printStackTrace();
		}	

	}
}
