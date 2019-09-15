package br.com.anteros.iot.app;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.diozero.util.SleepUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.core.utils.Assert;
import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.iot.Actuable;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.support.MqttHelper;

public class AnterosIOTService implements Runnable, MqttCallback, MqttCallbackExtended {

	private static final String RELOAD = "reload";

	private static final String UPDATE_CONFIG = "updateConfig";

	private static final String FILE = "file";

	private static final String STREAM = "stream";

	private static final String ACTION = "action";

	private static final String TYPE = "type";

	private static final String CONFIG = "config";

	private static final Logger LOG = LoggerProvider.getInstance().getLogger(AnterosIOTService.class.getName());

	private String deviceName;
	private File fileConfig;
	private AbstractDeviceController deviceController;
	private String hostMqtt;
	private String port;
	private String username;
	private String password;
	private MqttAsyncClient client;
	private AnterosIOTServiceListener serviceListener;
	private InputStream streamConfig;
	private Set<Class<? extends Actuable>> actuators = new HashSet<>();

	private String actionsTopic;
	public static final String HEARTBEAT = "/heartbeat";
	public static final String BOOT = "/boot";
	public static final String ERRORS = "/errors";

	public AnterosIOTService(String deviceName, String hostMqtt, String port, String username, String password,
			File config, InputStream streamConfig, AnterosIOTServiceListener serviceListener,
			Class<? extends Actuable>[] actuators) {
		this.deviceName = deviceName;
		this.fileConfig = config;
		this.hostMqtt = hostMqtt;
		this.port = port;
		this.username = username;
		this.password = password;
		this.serviceListener = serviceListener;
		this.streamConfig = streamConfig;
		if (actuators != null)
			this.actuators.addAll(Arrays.asList(actuators));
	}

	/**
	 * -name master -host-mqtt 192.168.0.1 -port 1883 -username admin -password
	 * admin -config /home/pi/iot.json
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String deviceName = getArgumentByName(args, "-name");
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

		Thread thread = new Thread(
				new AnterosIOTService(deviceName, hostMqtt, port, username, password, configFile, null, null, null));
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

	@Override
	public void run() {

		LOG.debug("Iniciando Anteros IOT Service...");

		serviceListener.onConnectingMqttServer();

		String broker = "tcp://" + hostMqtt + ":" + (port == null ? 1883 : port);
		String clientId = deviceName;
		actionsTopic = "/" + deviceName;

		LOG.info("Conectando servidor broker MQTT do device controller... " + broker);

		try {
			client = MqttHelper.createAndConnectMqttClient(broker, clientId, username, password, true, true, this);
		} catch (MqttException e1) {
			String msg = "Ocorreu uma falha ao criar um cliente mqtt. O Sistema não continuará a inicialização.";
			LOG.error(msg);
			serviceListener.onErrorConnectingMqttServer(msg);
			e1.printStackTrace();
		}

		try {
			startService();
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

		while (true) {

			if (this.client.isConnected()) {
				try {
					Boolean controllerRunning = deviceController != null ? deviceController.getRunning() : false;
					MqttHelper.publishHeartBeat(deviceName, "alive", controllerRunning, client);
					LOG.debug("Mensagem de Heart Beat publicada");
				} catch (MqttException e) {
					LOG.error("Falha ao publicar mensagem Heart Beat");
					e.printStackTrace();
				}
			}

			SleepUtil.sleepMillis(15000);
		}

	}

	private void startService() throws JsonParseException, JsonMappingException, IOException {
		if ((fileConfig != null || streamConfig != null) && client != null) {
			LOG.info("Lendo configuração e criando device controller...");
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			if (serviceListener != null) {
				serviceListener.onAddSubTypeNames(mapper);
			}
			try {
				deviceController = AnterosIOTConfiguration.newConfiguration().objectMapper(mapper)
						.registerActuators(actuators).deviceName(deviceName).hostMqtt(hostMqtt).port(port)
						.username(username).password(password).serviceListener(serviceListener).configure(streamConfig)
						.configure(fileConfig).buildDevice();
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

		} else {
			LOG.info(
					"Nenhuma configuração foi encontrada para criar o device controller ou o client mqtt não está conectado.");
		}

		if (deviceController != null) {
			serviceListener.onStartDeviceController();
			deviceController.start();
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

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonMessage = null;
		try {
			jsonMessage = mapper.readTree(message.getPayload());
		} catch (IOException e) {
			MqttHelper.publishError(e, deviceName, client);
			e.printStackTrace();
		}

		LOG.info("Mensagem recebida: " + jsonMessage);

		if (topic.equals("/" + deviceName)) {
			LOG.info("Ação recebida no guardian");

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
						newConfigFile = new File("/home/versatil/versatil-iot/conf/iot_config.json");
					}
				}

				if (newConfigStream != null) {
					this.streamConfig = newConfigStream;
				} else {
					this.fileConfig = newConfigFile;
				}

				serviceListener.onStopDeviceController();
				if (deviceController != null)
					deviceController.stop();
				SleepUtil.sleepMillis(1000);
				try {
					startService();
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
					startService();
				} catch (Exception e) {
					String payload = "{\"status\": \"error\",\"message\": \"" + e.getMessage() + "\"}";
					MqttMessage msg = new MqttMessage(payload.getBytes());
					msg.setQos(1);
					client.publish("/" + deviceName + "/notify", msg);
					MqttHelper.publishError(e, deviceName, client);
				}
			} else {
				LOG.info("Ação '" + action + "' inválida");
			}
		}

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		try {
			client.subscribe(actionsTopic, 1);
		} catch (MqttException e) {
			e.printStackTrace();
		}
		LOG.info("Conectado");
	}
}
