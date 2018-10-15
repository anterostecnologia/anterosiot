package br.com.anteros.iot.app;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import br.com.anteros.iot.Device;
import br.com.anteros.iot.RemoteMasterDeviceController;
import br.com.anteros.iot.SlaveDeviceController;
import br.com.anteros.iot.actuators.LampOrBulbActuator;
import br.com.anteros.iot.controllers.SlaveControllerRPi;
import br.com.anteros.iot.controllers.remote.RemoteMasterControllerRPi;
import br.com.anteros.iot.support.Pi4JHelper;
import br.com.anteros.iot.things.LampOrBulb;
import br.com.anteros.iot.things.devices.IpAddress;
import br.com.anteros.iot.things.devices.RaspberryPI;

public class Slave2App {

	public static void main(String[] args) {

//
//		String broker = "tcp://192.168.1.152:1883";
//		String clientId = "ServidorSlave2";
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
//			client.subscribe(new String[] {"ServidorSlave2"});
//
//			/*
//			 * 2 dispositivos
//			 */
//			Device masterDevice = RaspberryPI.of("ServidorMaster", IpAddress.of("192.168.1.152"));
//			Device slave2Device = RaspberryPI.of("ServidorSlave2", IpAddress.of("192.168.1.154"));
//
//			/*
//			 * Um atuador
//			 */
//			LampOrBulbActuator lampOrBulbActuator = new LampOrBulbActuator();
//
//			/*
//			 * Um controlador master remoto
//			 */
//			RemoteMasterDeviceController remoteMaster = RemoteMasterControllerRPi.Builder.create()
//					.device(masterDevice).build();
//			/*
//			 * Controlador slave 2
//			 */
//			SlaveDeviceController slave2 = SlaveControllerRPi.Builder.create().device(slave2Device).clientMqtt(client)
//					.master(remoteMaster).build();
//
//			/*
//			 * Uma coisa
//			 */
//			LampOrBulb lampOrBulb = LampOrBulb.of("Lampada",Pi4JHelper.GPIO_01);
//
//			/*
//			 * Inicia o slave2
//			 */
//			slave2.addThings(lampOrBulb);
//			slave2.registerActuator(lampOrBulbActuator);
//			slave2.start();
//
//		} catch (MqttException e) {
//			e.printStackTrace();
//		}

	}

}
