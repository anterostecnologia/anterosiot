package br.com.anteros.iot.things.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.anteros.iot.Actuable;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.actuators.MemoryPlcActuator;
import br.com.anteros.iot.app.AnterosIOTService;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.domain.devices.MasterComputerNode;
import br.com.anteros.iot.domain.plant.PlaceNode;
import br.com.anteros.iot.domain.plant.PlantNode;
import br.com.anteros.iot.domain.things.PlcNode;
import br.com.anteros.iot.domain.things.parts.MemoryPlcNode;
import br.com.anteros.iot.protocol.modbus.type.CollectType;
import br.com.anteros.iot.protocol.modbus.type.ModifyType;
import br.com.anteros.iot.things.devices.IpAddress;
import br.com.anteros.iot.triggers.Trigger;


public class AnterosIotService implements AnterosIOTServiceListener {

	public AnterosIotService() {
	}

	public static void main(String[] args) throws JsonProcessingException {

		AnterosIotService service = new AnterosIotService();

		new Thread(new AnterosIOTService("master", "master", "10.0.1.1", "1883", "admin", "L657NKJH", null,
				null, buildConfiguration(), service, new Class[] { MemoryPlcActuator.class })).start();
	}

	private static InputStream buildConfiguration() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		PlantNode empresaNode = new PlantNode("cristal", "Cristal");
		PlaceNode envaseNode = new PlaceNode("envase_agua", "Envase de água");

		empresaNode.addChildren(envaseNode);

		/**
		 * MASTER
		 */

		MasterComputerNode masterNode = new MasterComputerNode("master", "Central");
		masterNode.setIpAddress(IpAddress.of("10.0.5.251"));
		masterNode.setTopicError("");

		PlcNode controladorTeste = new PlcNode("clp_inspecao_agua", "CLP Inspeção de água",
				"TCP/IP", "10.0.6.240", 502, 50, 1500, 1);
		
//		MemoryPlcNode memoriaSensorPresenca = new MemoryPlcNode("memoria_sensor_presenca",
//				"Memoria sensor presença", 2, CollectType.INPUTREGISTER, ModifyType.WRITE);
//		
//		MemoryPlcNode memoriaPistao = new MemoryPlcNode("memoria_pistao", "MEMORIA_PISTAO", 3, CollectType.INPUTREGISTER, ModifyType.WRITE);

//		controladorTeste.addChild(memoriaSensorPresenca);
//		controladorTeste.addChild(memoriaPistao);

		masterNode.addThingsToController(controladorTeste);

		envaseNode.addChildren(masterNode, controladorTeste);
		
		System.out.println(mapper.writeValueAsString(empresaNode));

		return new ByteArrayInputStream(mapper.writeValueAsBytes(empresaNode));

	}

	public void onFireTrigger(Trigger source, Object value) {
		throw new RuntimeException();
	}

	public void onAddSubTypeNames(ObjectMapper mapper) {

	}

	@Override
	public void onBeforeStartService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAfterStartService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectingMqttServer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onErrorConnectingMqttServer(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Class<? extends Actuable>> getNewActuatorsToRegister() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onBeforeBuildDeviceController() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAfterBuildDeviceController() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopDeviceController() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStartCollectors(AbstractDeviceController abstractDeviceController) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopCollectors(AbstractDeviceController abstractDeviceController) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartDeviceController(Device device) {
		// TODO Auto-generated method stub
		
	}

}
