package br.com.anteros.iot.app;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import br.com.anteros.iot.Device;
import br.com.anteros.iot.RemoteMasterDeviceController;
import br.com.anteros.iot.SlaveDeviceController;
import br.com.anteros.iot.actuators.LedActuator;
import br.com.anteros.iot.actuators.MagneticLockActuator;
import br.com.anteros.iot.controllers.SlaveControllerRPi;
import br.com.anteros.iot.controllers.remote.RemoteMasterControllerRPi;
import br.com.anteros.iot.support.Pi4JHelper;
import br.com.anteros.iot.things.MagneticLock;
import br.com.anteros.iot.things.devices.IpAddress;
import br.com.anteros.iot.things.devices.RaspberryPI;

public class Slave1App {

	public static void main(String[] args) {

//		String broker = "tcp://192.168.1.152:1883";
//		String clientId = "ServidorSlave1";
//		MemoryPersistence persistence = new MemoryPersistence();
//		
//		try {
//			MqttClient client = new MqttClient(broker, clientId, persistence);
//			
//			MqttConnectOptions connOpts = new MqttConnectOptions();
//			connOpts.setAutomaticReconnect(true);
//			connOpts.setCleanSession(true);
//			client.connect(connOpts);
//			
//			client.subscribe(new String[] {"ServidorSlave1"});
//
//			/*
//			 * 2 dispositivos
//			 */
//			Device masterDevice = RaspberryPI.of("ServidorMaster", IpAddress.of("192.168.1.152"));
//			Device slave1Device = RaspberryPI.of("ServidorSlave1", IpAddress.of("192.168.1.153"));
//
//			/*
//			 * Um atuador
//			 */
//			MagneticLockActuator magneticLockActuator = new MagneticLockActuator();
//
//			/*
//			 * Um controlador master remoto
//			 */
//			RemoteMasterDeviceController remoteMaster = RemoteMasterControllerRPi.Builder.create()
//					.device(masterDevice).build();
//			/*
//			 * Controlador slave 1
//			 */
//			SlaveDeviceController slave1 = SlaveControllerRPi.Builder.create().device(slave1Device).clientMqtt(client)
//					.master(remoteMaster).build();
//
//			/*
//			 * Uma coisa
//			 */
//			 MagneticLock magneticLock = MagneticLock.of("FechaduraMagnetica", Pi4JHelper.GPIO_01,1000);
//			/*
//			 * Inicia o slave1
//			 */
//			slave1.addThings(magneticLock);
//			slave1.registerActuator(magneticLockActuator);
//			slave1.start();
//
//		} catch (MqttException e) {
//			e.printStackTrace();
//		}

	}
}
