package br.com.anteros.iot.app;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.diozero.util.SleepUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.anteros.client.mqttv3.IMqttDeliveryToken;
import br.com.anteros.client.mqttv3.MqttCallback;
import br.com.anteros.client.mqttv3.MqttCallbackExtended;
import br.com.anteros.client.mqttv3.MqttException;
import br.com.anteros.client.mqttv3.MqttMessage;
import br.com.anteros.client.mqttv3.MqttPersistenceException;
import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.core.utils.Assert;
import br.com.anteros.core.utils.IOUtils;
import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.iot.Actuable;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.domain.plant.PlaceNode;
import br.com.anteros.iot.domain.plant.PlantNode;
import br.com.anteros.iot.support.AnterosMqttClient;
import br.com.anteros.iot.support.MqttHelper;
import br.com.anteros.iot.support.utils.StaticUtil;

public class AnterosIOTService implements Runnable, MqttCallback, MqttCallbackExtended {

	public static final String RELOAD = "reload";

	public static final String UPDATE_CONFIG = "updateConfig";

	public static final String FILE = "file";

	public static final String STREAM = "stream";

	public static final String ACTION = "action";

	public static final String TYPE = "type";

	public static final String CONFIG = "config";

	private static final String MASTER = "master";

	public static final Logger LOG = LoggerProvider.getInstance().getLogger(AnterosIOTService.class.getName());

	public AtomicBoolean running = new AtomicBoolean(false);

	private String deviceName;
	private String deviceType;
	private String configFilePath;
	private File fileConfig;
	private InputStream streamConfig;
	private AbstractDeviceController deviceController;
	private String hostMqtt;
	private String port;
	private String username;
	private String password;
	private AnterosMqttClient client;
	private AnterosIOTServiceListener serviceListener;
	private Set<Class<? extends Actuable>> actuators = new HashSet<>();

	private String actionsTopic;
	public static final String HEARTBEAT_TOPIC = "/heartbeat";
	public static final String BOOT_TOPIC = "/boot";
	public static final String ERRORS_TOPIC = "/errors";
	public static final String UPDATE_CONFIG_TOPIC = "/updateConfig";

	private ObjectMapper mapper;

	private boolean alreadyConnectedOnce = false;

	public AnterosIOTService(String deviceName, String deviceType, String hostMqtt, String port, String username,
			String password, String configFilePath, File config, InputStream streamConfig,
			AnterosIOTServiceListener serviceListener, Class<? extends Actuable>[] actuators) {
		this.deviceName = deviceName;
		this.deviceType = deviceType;
		this.fileConfig = config;
		this.streamConfig = streamConfig;
		this.configFilePath = configFilePath;
		this.hostMqtt = hostMqtt;
		this.port = port;
		this.username = username;
		this.password = password;
		this.serviceListener = serviceListener;
		if (actuators != null)
			this.actuators.addAll(Arrays.asList(actuators));
	}

	/**
	 * -name master -type master -host-mqtt 192.168.0.1 -port 1883 -username admin
	 * -password admin -config /home/pi/iot.json
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String deviceName = getArgumentByName(args, "-name");
		String deviceType = getArgumentByName(args, "-type");
		String hostMqtt = getArgumentByName(args, "-host-mqtt");
		String port = getArgumentByName(args, "-port");
		String username = getArgumentByName(args, "-username");
		String password = getArgumentByName(args, "-password");
		String config = getArgumentByName(args, "-config");
		Assert.notNull(deviceName, "Informe ao menos o nome do device.");
		Assert.notNull(hostMqtt, "Informe o nome do servidor mqtt.");

		File configFile = null;

		if (StringUtils.isNotEmpty(config)) {
			configFile = new File(config);
		}

		Thread thread = new Thread(new AnterosIOTService(deviceName, deviceType, hostMqtt, port, username, password,
				null, configFile, null, null, null));
		thread.start();
		thread.setName("Anteros IOT Service");
	}

	public static String getArgumentByName(String[] args, String name) {
		if (args == null || args.length == 0) {
			return null;
		}
		int index = 0;
		for (String arg : args) {
			if (arg.equalsIgnoreCase(name)) {
				if ((args.length - 1) >= index + 1) {
					return args[index + 1];
				} else {
					return null;
				}
			}
			index++;
		}
		return null;
	}

	public void stop(){
		running.set(false);
		if (deviceController != null) {
			if (this.client != null) {
				try {
					this.client.disconnect();
				} catch (MqttException e) {
				}
				this.client = null;
			}
			serviceListener.onStopDeviceController();
			deviceController.stop();
		}
	}

	@Override
	public void run() {

		running.set(true);
		try {
			LOG.debug("Iniciando Anteros IOT Service...");

			serviceListener.onConnectingMqttServer();

			String broker = "tcp://" + hostMqtt + ":" + (port == null ? 1883 : port);
			String clientId = deviceName.split("-")[0];
			actionsTopic = "/" + deviceName;

			LOG.info("Conectando servidor broker MQTT do device controller... " + broker);
			client = MqttHelper.createMqttClient(broker, clientId, username, password, true, true, this);
			client.connect();
		} catch (MqttException e1) {
			String msg = "Ocorreu uma falha ao criar um cliente mqtt. O Sistema não continuará a inicialização.";
			LOG.error(msg);
			serviceListener.onErrorConnectingMqttServer(msg);
			e1.printStackTrace();
		}
		try {
			startController();
			LOG.debug("Serviço inicializado. Device running = " + deviceController.getRunning());
		} catch (Exception e1) {
			if (client.isConnected()) {
				String payload = "{\"status\": \"error\",\"message\": \"" + e1.getMessage() + "\"}";
				MqttMessage msg = new MqttMessage(payload.getBytes());
				msg.setQos(1);
				try {
					client.publish("/" + deviceName + "/notify", msg);
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
		}

		while (running.get()) {

			if (this.client != null && this.client.isConnected()) {
				try {
					Boolean controllerRunning = deviceController != null ? deviceController.getRunning() : false;
					String hostAddress = null;

					Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
					for (NetworkInterface netint : Collections.list(nets)) {
						if (netint.getName().equals("eth0")) {
							Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
							InetAddress inetAddress = inetAddresses.nextElement();
							hostAddress = inetAddress.getHostAddress();
							break;
						}
					}

					MqttHelper.publishHeartBeat(deviceName, deviceType, "alive", controllerRunning, hostAddress,
							client);
					LOG.debug("Mensagem de Heart Beat publicada");
				} catch (MqttException e) {
					LOG.error("Falha ao publicar mensagem Heart Beat");
					e.printStackTrace();
				} catch (SocketException e) {
					e.printStackTrace();
				}
			} else if (this.client != null && alreadyConnectedOnce) {
				try {
					this.client.reconnect();
				} catch (MqttException e) {
					if (e.getReasonCode() == MqttException.REASON_CODE_CONNECT_IN_PROGRESS || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING) {
						SleepUtil.sleepMillis(2000);
					} else {
						e.printStackTrace();
					}
				}
			}

			SleepUtil.sleepMillis(15000);
		}



	}

	private void stopController(){
		serviceListener.onStopDeviceController();
		if (deviceController != null)
			deviceController.stop();
		SleepUtil.sleepMillis(5000);
	}

	private void startController() throws JsonParseException, JsonMappingException, IOException {
		if ((fileConfig != null || streamConfig != null)) {
			LOG.info("Lendo configuração e criando device controller...");
			if (mapper == null) {
				mapper = new ObjectMapper();
				mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
				mapper.enable(SerializationFeature.INDENT_OUTPUT);
			}
			if (serviceListener != null) {
				serviceListener.onAddSubTypeNames(mapper);
			}
			try {
				deviceController = AnterosIOTConfiguration.newConfiguration().objectMapper(mapper)
						.registerActuators(actuators).deviceName(deviceName).hostMqtt(hostMqtt).port(port)
						.username(username).password(password).serviceListener(serviceListener)
						.configFilePath(configFilePath).configure(streamConfig).configure(fileConfig).buildDevice();
			} catch (Exception e) {
				if (this.client.isConnected()) {
					String msg = "{\"status\": \"error\",\"message\": \"" + e.getMessage() + "\"}";
					MqttMessage message = new MqttMessage(msg.getBytes());
					try {
						client.publish("/" + deviceName + "/notify", message);
					} catch (MqttPersistenceException e1) {
						e1.printStackTrace();
					} catch (MqttException e1) {
						e1.printStackTrace();
					}
				}
				throw e;
			}

			if (deviceController != null) {
				serviceListener.onStartDeviceController(deviceController.getDevice());
				deviceController.start();
				serviceListener.onAfterBuildDeviceController(deviceController);
			}

		} else {
			LOG.error(
					"Nenhuma configuração foi encontrada para criar o device controller ou o client mqtt não está conectado.");
		}

	}

	private void propagateConfigurationToThings(ObjectMapper mapper, File fileConfig, InputStream streamConfig)
			throws JsonParseException, JsonMappingException, IOException, MqttPersistenceException, MqttException {
		PlantItemNode node = deviceController.getNode();

		if (node == null) {
			if (fileConfig != null) {
				node = mapper.readValue(fileConfig, PlantItemNode.class);
			}
			if (streamConfig != null) {
				node = mapper.readValue(streamConfig, PlantItemNode.class);
			}
		}

		if (node != null && this.deviceType.equals(MASTER)) {
			if (!(node instanceof PlantNode)) {
				throw new DeviceControllerException("O nó inicial da árvore de configuração deve ser um Local.");
			}

			propagate(mapper, node, node);
		}
	}

	private void propagate(ObjectMapper mapper, PlantItemNode node, PlantItemNode rootNode)
			throws JsonProcessingException, MqttException, MqttPersistenceException {
		Set<PlantItemNode> items = node.getItems();
		for (PlantItemNode item : items) {
			if ((item instanceof ThingNode && ((ThingNode) item).needsPropagation())
					|| (item instanceof DeviceNode && ((DeviceNode) item).needsPropagation())) {

				String parsedConfig = "";
				if (item instanceof ThingNode) {
					parsedConfig = ((ThingNode) item).parseConfig(mapper, item);
				} else {

//					parsedConfig = ((DeviceNode) item).parseConfig(mapper, rootNode);

					if (fileConfig != null) {
						parsedConfig = StaticUtil.readLineByLineOfFile(fileConfig.getPath());
					} else if (streamConfig != null) {
						try {
							parsedConfig = StaticUtil.readLineByLineOfFile(StaticUtil
									.inputStreamToFile(streamConfig, "src/main/resources/targetFile.tmp").getPath());
						} catch (Exception e) {
							MqttHelper.publishError(e, deviceName, client);
							e.printStackTrace();
						}
					}
				}

				String topicPropagation = "/" + item.getItemName();
				String jsonNode = "{\"action\": \"updateConfig\",\"type\":\"stream\",\"config\": " + parsedConfig + "}";
				MqttMessage propagationNode = new MqttMessage(jsonNode.getBytes());
				client.publish(topicPropagation, propagationNode);
			} else if (item instanceof PlaceNode || item instanceof PlantNode) {
				propagate(mapper, item, rootNode);
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		serviceListener.onStopDeviceController();
		super.finalize();
	}

	@Override
	public void connectionLost(Throwable cause) {
		try {
			this.client.reconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws MqttPersistenceException, MqttException {

		JsonNode jsonMessage = null;
		try {
			if (mapper == null) {
				mapper = new ObjectMapper();
				mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
				mapper.enable(SerializationFeature.INDENT_OUTPUT);

				if (serviceListener != null) {
					serviceListener.onAddSubTypeNames(mapper);
				}
			}
			jsonMessage = mapper.readTree(message.getPayload());
		} catch (Exception e) {
			MqttHelper.publishError(e, deviceName, client);
			e.printStackTrace();
		}

		LOG.info("Mensagem recebida: " + jsonMessage);

		if (topic.equals("/" + deviceName)) {
			LOG.info("Ação recebida no GUARDIÃO");

			String action = jsonMessage.has(ACTION) ? jsonMessage.get(ACTION).toString() : null;

			if (action != null && action.contains(UPDATE_CONFIG)) {

				LOG.info(jsonMessage.toString());
				LOG.info("Atualizando configuração no device " + deviceName);

				ByteArrayInputStream newConfigStream = null;
				File newConfigFile = null;

				if (jsonMessage.has(TYPE)) {
					if (jsonMessage.get(TYPE).toString().contains(STREAM)) {
						newConfigStream = jsonMessage.has(CONFIG)
								? new ByteArrayInputStream(jsonMessage.get(CONFIG).toString().getBytes())
								: null;
					} else if (jsonMessage.get(TYPE).toString().contains(FILE)) {
						newConfigFile = jsonMessage.has(CONFIG) ? new File(jsonMessage.get(CONFIG).toString()) : null;
					} else if (jsonMessage.get(TYPE).toString().contains(RELOAD)) {
						newConfigFile = new File(configFilePath);
					}
				}

				if (newConfigStream != null) {
					this.streamConfig = newConfigStream;
				} else {
					this.fileConfig = newConfigFile;
				}

				stopController();

				try {
					startController();
					MqttMessage msg = new MqttMessage();
					msg.setQos(1);
					if (streamConfig != null) {
						msg.setPayload(IOUtils.toByteArray(streamConfig));
						this.streamConfig.close();
					} else {
						FileInputStream inputStream = new FileInputStream(fileConfig);
						msg.setPayload(IOUtils.toByteArray(inputStream));
						inputStream.close();
					}
					LOG.info("Enviando configuração para o guardião no tópico " + AnterosIOTService.UPDATE_CONFIG_TOPIC
							+ "/" + deviceName);
					client.publish(AnterosIOTService.UPDATE_CONFIG_TOPIC + "/" + deviceName, msg);

					propagateConfigurationToThings(mapper, fileConfig, streamConfig);
					serviceListener.onUpdateConfiguration();
				} catch (Exception e) {
					String payload = "{\"status\": \"error\",\"message\": \"" + e.getMessage() + "\"}";
					MqttMessage msg = new MqttMessage(payload.getBytes());
					msg.setQos(1);
					client.publish("/" + deviceName + "/notify", msg);
					MqttHelper.publishError(e, deviceName, client);
					e.printStackTrace();
				}

			} else if (action != null && action.contains("restartService")) {
				LOG.info("Reiniciando controller do device " + deviceName);
				MqttHelper.publishBoot(deviceName, client);
				deviceController.stop();
				SleepUtil.sleepMillis(5000);
				try {
					startController();
				} catch (Exception e) {
					String payload = "{\"status\": \"error\",\"message\": \"" + e.getMessage() + "\"}";
					MqttMessage msg = new MqttMessage(payload.getBytes());
					msg.setQos(1);
					client.publish("/" + deviceName + "/notify", msg);
					MqttHelper.publishError(e, deviceName, client);
				}
			} else {
				LOG.error("Ação '" + action + "' inválida");
			}
		}

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		if (!alreadyConnectedOnce) {
			alreadyConnectedOnce = true;
		}
		try {
			client.subscribe(actionsTopic, 1);
		} catch (MqttException e) {
			e.printStackTrace();
		}
		LOG.info("Conectado");
	}
}
