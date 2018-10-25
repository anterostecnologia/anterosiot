package br.com.anteros.iot.app;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.anteros.iot.domain.PartNode;
import br.com.anteros.iot.domain.devices.MasterDeviceRPiNode;
import br.com.anteros.iot.domain.devices.SlaveRPiNode;
import br.com.anteros.iot.domain.plant.PlaceNode;
import br.com.anteros.iot.domain.plant.PlantNode;
import br.com.anteros.iot.domain.things.LampSonoffNode;
import br.com.anteros.iot.domain.things.RFIDModel;
import br.com.anteros.iot.domain.things.RFIDReaderNode;
import br.com.anteros.iot.domain.things.SemaphoreNode;
import br.com.anteros.iot.domain.things.parts.GreenLEDSemaphorePartNode;
import br.com.anteros.iot.domain.things.parts.RedLEDSemaphorePartNode;
import br.com.anteros.iot.support.Pi4JHelper;
import br.com.anteros.iot.things.devices.IpAddress;

public class MasterApp {

	public static void main(String[] args) throws JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		PlaceNode portariaNode = new PlaceNode("portaria", "Portaria central");
		PlaceNode blocoANode = new PlaceNode("blocoA", "Bloco A");
		PlaceNode blocoBNode = new PlaceNode("blocoB", "Bloco B");

		PlantNode empresaNode = new PlantNode("empresa", "Empresa");

		empresaNode.addChildren(portariaNode, blocoANode, blocoBNode);
		
		
		/**
		 * MASTER
		 */

		MasterDeviceRPiNode masterNode = new MasterDeviceRPiNode("master", "Central");
		masterNode.setIpAddress(IpAddress.of("10.0.0.152"));

//		TemperatureOneWireNode temperatureNode = new TemperatureOneWireNode("sensorTemperatura",
//				"Sensor de temperatura", "28-031671d27bff", null,
//				TemperatureScale.CELSIUS);
		
		

//		PresenceDetectorNode presenceNode = new PresenceDetectorNode("detectorPresenca", "Sensor de presença",
//				Pi4JHelper.GPIO_04, null);
		
		
		LampSonoffNode lampSonoffNode = new LampSonoffNode("Lampada","Lampada","portaria/lampada_sonoff");

//		portariaNode.addChildren(masterNode, lampSonoffNode);
//		
//		masterNode.addThingsToController(lampSonoffNode);
		
		
		/**
		 * SLAVE 1
		 */
		

		SlaveRPiNode slave1Node = new SlaveRPiNode("slave1", "Slave 1");
		slave1Node.setIpAddress(IpAddress.of("10.0.0.155"));
		
		blocoANode.addChild(slave1Node);
		
		// RFID
		
		RFIDReaderNode rfidReaderNode = new RFIDReaderNode("rfidCard", "Leitor de cartão", null, RFIDModel.PN532);
		
		
		// Semaforo
		
		SemaphoreNode semaphoreNode = new SemaphoreNode("Semaforo","Semaforo");
		PartNode redLed = new RedLEDSemaphorePartNode("LedRed","Led Vermelho", Pi4JHelper.GPIO_06);
		PartNode greenLed = new GreenLEDSemaphorePartNode("LedGreen","Led Verde",Pi4JHelper.GPIO_05);
		semaphoreNode.addChild(redLed);
		semaphoreNode.addChild(greenLed);

		slave1Node.addThingsToController(rfidReaderNode, semaphoreNode);
//				
//		/**
//		 * SLAVE 2
//		 */
//		
//		SlaveRPiNode slave2Node = new SlaveRPiNode("slave2", "Slave 2");
//		slave2Node.setIpAddress(IpAddress.of("192.168.1.153"));
		
		
//		
		
//		MagneticLockNode magneticLockNode = new MagneticLockNode("FechaduraMagnetica", "Fechadura magnética", Pi4JHelper.GPIO_01, 1000);
		
		
		
//		blocoBNode.addChildren(slave2Node,lampOrBulbNode);
//		slave2Node.addThingsToController(lampOrBulbNode);
		
		// RECEPTOR
		
//		/**
//		 * SLAVE 3
//		 */
//		SlaveRPiNode slave3Node = new SlaveRPiNode("slave3", "Slave 3");
//		slave3Node.setIpAddress(IpAddress.of("192.168.1.154"));
//		
//		blocoBNode.addChild(slave3Node);
//		
//		CameraQRCodeReaderNode cameraNode = new CameraQRCodeReaderNode();
//		blocoBNode.addChild(cameraNode);
//		
//		slave3Node.addThingsToController(cameraNode);

		

		System.out.println(mapper.writeValueAsString(empresaNode));
//		
//		
//		
//		String broker = "tcp://192.168.1.152:1883";
//		String clientId = "ServidorMaster";
//		MemoryPersistence persistence = new MemoryPersistence();
//		
//		try {
//			System.out.println("Conectando servidor broker MQTT...");
//			MqttClient client = new MqttClient(broker, clientId, persistence);
//			
//			MqttConnectOptions connOpts = new MqttConnectOptions();
//			connOpts.setAutomaticReconnect(true);
//			connOpts.setCleanSession(true);
//			client.connect(connOpts);
//			System.out.println("Conectado servidor broker MQTT");
//			
//			client.subscribe(new String[] {"ServidorMaster"});
//			
//			System.out.println("Subscribe em ServidorMaster...");
//			/*
//			 * 3 dispositivos
//			 */
//			Device masterDevice = RaspberryPI.of("ServidorMaster",IpAddress.of("192.168.1.152"));
//			Device slave1Device = RaspberryPI.of("ServidorSlave1",IpAddress.of("192.168.1.153"));
//			Device slave2Device = RaspberryPI.of("ServidorSlave2",IpAddress.of("192.168.1.154"));
//
//			/*
//			 * Um atuador
//			 */
//			LedActuator ledActuator = new LedActuator();
//			
//			/*
//			 * Um controlador master
//			 */
//			MasterDeviceController master = MasterControllerRPi.Builder.create().clientMqtt(client).device(masterDevice).atuactors(ledActuator)
//					.build();
//			/*
//			 * Dois controladores slaves
//			 */
//			
//			RemoteSlaveDeviceController slave1 = RemoteSlaveControllerRpi.Builder.create().device(slave1Device).master(master).build();
//			RemoteSlaveDeviceController slave2 = RemoteSlaveControllerRpi.Builder.create().device(slave2Device).master(master).build();
//
//			master.addChildDeviceController(slave1).addChildDeviceController(slave2);
//			
//			/*
//			 * Uma coisa
//			 */
//			Semaphore semaphore = new Semaphore("Semaforo");
//			Part redLed = RedLEDSemaphorePart.of("Led Red",semaphore, Pi4JHelper.GPIO_01);
//			Part greenLed = GreenLEDSemaphorePart.of("Led Green",semaphore,Pi4JHelper.GPIO_02);
//			semaphore.addPart(redLed);
//			semaphore.addPart(greenLed);
//			
//			/*
//			 * Inicia o master
//			 */
//			master.addThings(semaphore);
//			master.registerActuator(ledActuator);
//			master.start();
//			
//		} catch (MqttException e) {
//			e.printStackTrace();
//		}	

	}
}
