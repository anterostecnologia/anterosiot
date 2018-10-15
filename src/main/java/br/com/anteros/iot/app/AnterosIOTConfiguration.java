package br.com.anteros.iot.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.anteros.iot.DefaultActuators;
import br.com.anteros.iot.MasterDeviceController;
import br.com.anteros.iot.RemoteMasterDeviceController;
import br.com.anteros.iot.SlaveDeviceController;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.controllers.remote.RemoteDeviceControllerFactory;
import br.com.anteros.iot.domain.DeviceMasterNode;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.domain.DeviceSlaveNode;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.domain.plant.PlantNode;
import br.com.anteros.iot.plant.Place;
import br.com.anteros.iot.plant.Plant;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.support.MqttHelper;

public class AnterosIOTConfiguration {

	private PlantItemNode node;
	private File fileConfig;
	private ObjectMapper mapper = new ObjectMapper();
	private String deviceName;
	private MqttClient clientMqtt;
	private String hostMqtt;
	private String port;
	private String username;
	private String password;
	private Plant currentPlant;

	private AnterosIOTConfiguration() {
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	public static AnterosIOTConfiguration newConfiguration() {
		return new AnterosIOTConfiguration();
	}

	public AnterosIOTConfiguration addItemNode(PlantItemNode node) {
		this.node = node;
		return this;
	}

	public AnterosIOTConfiguration configure(String config) {
		configure(new File(config));
		return this;
	}

	public AnterosIOTConfiguration configure(File config) {
		this.fileConfig = config;
		return this;
	}

	public AbstractDeviceController buildDevice() throws JsonParseException, JsonMappingException, IOException {
		if (fileConfig != null) {
			node = mapper.readValue(fileConfig, PlantItemNode.class);
		}
		AbstractDeviceController deviceResult = null;
		if (node != null) {
			if (!(node instanceof PlantNode)) {
				throw new DeviceControllerException("O nó inicial da árvore de configuração deve ser  name "
						+ deviceName + " não encontrado na configuração.");
			}

			currentPlant = ((PlantNode) node).getInstanceOfPlant();

			PlantItemNode itemNode = node.findNodeByName(deviceName);
			if (itemNode == null) {
				throw new DeviceControllerException("Device name " + deviceName + " não encontrado na configuração.");
			}
			if (!(itemNode instanceof DeviceNode)) {
				throw new DeviceControllerException(
						"Device name " + deviceName + " não é um dispositivo controlador na árvore de configuração.");
			}
			
			DeviceNode deviceNode = (DeviceNode) itemNode;

			String broker = "tcp://" + hostMqtt + ":" + (port == null ? 1883 : port);

			String clientId = deviceName + "_controller";
			MemoryPersistence persistence = new MemoryPersistence();

			System.out.println("Conectando servidor broker MQTT..." + broker);
			
			MqttClient clientMqtt = null;
			try {
				clientMqtt = MqttHelper.createAndConnectMqttClient(broker, clientId, "", "", true, true);
			} catch (MqttException e1) {
				e1.printStackTrace();
			}			

			deviceResult = ((DeviceNode) deviceNode).getInstanceOfDeviceController(clientMqtt,
					currentPlant, new DefaultActuators());

			if (deviceResult instanceof MasterDeviceController) {
				List<PlantItemNode> slaves = new ArrayList<>();
				node.findNodesByType(DeviceSlaveNode.class, slaves);

				for (PlantItemNode slave : slaves) {
					if (slave instanceof DeviceSlaveNode) {
						
						MqttClient remoteClientMqtt = null;
						try {
							remoteClientMqtt = MqttHelper.createAndConnectMqttClient(broker, deviceName + "_remoteController", "", "", true, true);
						} catch (MqttException e1) {
							e1.printStackTrace();
						}							
						((MasterDeviceController) deviceResult)
								.addChildDeviceController(RemoteDeviceControllerFactory.createSlaveFrom(remoteClientMqtt,
										(MasterDeviceController) deviceResult, (DeviceSlaveNode) slave, currentPlant));
					}
				}
			} else {
				List<PlantItemNode> masters = new ArrayList<>();
				node.findNodesByType(DeviceMasterNode.class, masters);
				if (masters.size()==0) {
					throw new DeviceControllerException(
							"Não foi encontrado nenhum Device Master na árvore de configuração.");
				}
				
				DeviceMasterNode master = (DeviceMasterNode) masters.iterator().next();

				MqttClient remoteClientMqtt = null;
				try {
					remoteClientMqtt = MqttHelper.createAndConnectMqttClient(broker, deviceName + "_remoteController", "", "", true, true);
				} catch (MqttException e1) {
					e1.printStackTrace();
				}	
				
				RemoteMasterDeviceController remoteMaster = RemoteDeviceControllerFactory.createMasterFrom(remoteClientMqtt, master, currentPlant);
				
				((SlaveDeviceController)deviceResult).setMaster(remoteMaster);
			}
			
			for (ThingNode thingNode : deviceNode.getThings()) {
				Thing thing = thingNode.getInstanceOfThing();
				deviceResult.addThings(thing);
				if (!(thing instanceof PlantItem)) {
					throw new DeviceControllerException("A coisa "+itemNode.getItemName()+" não é um item da planta.");
				}				
				Place place = (Place) currentPlant.getItemByName(thingNode.getItemNodeOwner().getItemName());
				place.addItems((PlantItem)thing);
			}
			
			/**
			 * Ouvindo mensagens MQTT para todas as coisas sendo contraladas e mais o próprio contralador
			 */			
			deviceResult.autoSubscribe();
		}

		return deviceResult;
	}

	public AnterosIOTConfiguration deviceName(String deviceName) {
		this.deviceName = deviceName;
		return this;
	}

	public AnterosIOTConfiguration clientMqtt(MqttClient clientMqtt) {
		this.clientMqtt = clientMqtt;
		return this;
	}

	public AnterosIOTConfiguration hostMqtt(String hostMqtt) {
		this.hostMqtt = hostMqtt;
		return this;
	}

	public AnterosIOTConfiguration port(String port) {
		this.port = port;
		return this;
	}

	public AnterosIOTConfiguration username(String username) {
		this.username = username;
		return this;
	}

	public AnterosIOTConfiguration password(String password) {
		this.password = password;
		return this;
	}

}
