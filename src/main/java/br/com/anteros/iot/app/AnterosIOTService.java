package br.com.anteros.iot.app;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

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
import br.com.anteros.iot.Action;
import br.com.anteros.iot.Actuable;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.domain.plant.PlaceNode;
import br.com.anteros.iot.plant.Place;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.support.MqttHelper;

public class AnterosIOTService implements Runnable, MqttCallback {

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

		Thread thread = new Thread(new AnterosIOTService(deviceName, hostMqtt, port, username, password, configFile, null, null, null));
		thread
				.start();
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
		String actionsTopic = "/"+deviceName;

		LOG.info("Conectando servidor broker MQTT do device controller... "+broker);

		try {
			client = MqttHelper.createAndConnectMqttClient(broker, clientId, username, password, true, true);
			client.setCallback(this);
			client.subscribe(actionsTopic, 1);
			LOG.info("Conectado ao "+broker);
		} catch (MqttException e1) {
			String msg ="Ocorreu uma falha ao criar um cliente mqtt. O Sistema não continuará a inicialização.";
			LOG.error(msg);
			serviceListener.onErrorConnectingMqttServer(msg);
			e1.printStackTrace();
		}

		try {
			startService();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		LOG.debug("Serviço inicializado. Device running = "+deviceController.getRunning());
		while (true) {
			try {
				Boolean controllerRunning = deviceController != null ? deviceController.getRunning() : false;
				MqttHelper.publishHeartBeat(deviceName,"alive",controllerRunning, client);				
				LOG.debug("Mensagem de Heart Beat publicada");
			} catch (MqttException e) {
				LOG.error("Falha ao publicar mensagem Heart Beat");
				e.printStackTrace();
			}

			SleepUtil.sleepMillis(5000);
		}

	}

	private void startService() throws JsonParseException, JsonMappingException, IOException {
		if ((fileConfig != null || streamConfig != null) && client != null && client.isConnected()) {
			LOG.info("Lendo configuração e criando device controller...");
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			if (serviceListener != null) {
				serviceListener.onAddSubTypeNames(mapper);
			}
			deviceController = AnterosIOTConfiguration.newConfiguration().objectMapper(mapper)
					.registerActuators(actuators).deviceName(deviceName).hostMqtt(hostMqtt).port(port)
					.username(username).password(password).serviceListener(serviceListener).configure(streamConfig)
					.configure(fileConfig).buildDevice();

		} else {
			LOG.info("Nenhuma configuração foi encontrada para criar o device controller ou o client mqtt não está conectado.");
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
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws MqttPersistenceException, MqttException {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonMessage = null;
		try {
			jsonMessage = mapper.readTree(message.getPayload());
		} catch (IOException e) {
			MqttHelper.publishError(e,deviceName,client);
			e.printStackTrace();
		}

		LOG.info("Mensagem recebida: " + jsonMessage);

		if (topic.equals("/"+deviceName)) {
			LOG.info("Ação recebida no guardian");

			String action = jsonMessage.has(ACTION) ? jsonMessage.get(ACTION).toString().replace("\"", "") : null;

			if (action != null && action.equals(UPDATE_CONFIG)) {

				LOG.info(jsonMessage.toString());
				LOG.info(jsonMessage.get(CONFIG).toString().replace("\"", ""));

				LOG.info("Atualizando configuração no device " + deviceName);

				ByteArrayInputStream newConfigStream = null;
				File newConfigFile = null;

				if (jsonMessage.has(TYPE)) {
					if (jsonMessage.get(TYPE).toString().equals(STREAM)) {
						newConfigStream = jsonMessage.has(CONFIG)
								? new ByteArrayInputStream(
										jsonMessage.get(CONFIG).toString().replace("\"", "").getBytes())
								: null;
					} else if (jsonMessage.get(TYPE).toString().equals(FILE)) {
						newConfigFile = jsonMessage.has(CONFIG)
								? new File(jsonMessage.get(CONFIG).toString().replace("\"", ""))
								: null;
					}
				}

				if (newConfigStream != null) {
					this.streamConfig = newConfigStream;
				} else {
					this.fileConfig = newConfigFile;
				}

				serviceListener.onStopDeviceController();
				deviceController.stop();
				SleepUtil.sleepMillis(1000);
				try {
					startService();
				} catch (IOException e) {
					MqttHelper.publishError(e,deviceName,client);
					e.printStackTrace();
				}

			} else if (action != null && action.equals("restartService")) {
				LOG.info("Reiniciando controller do device " + deviceName);
				MqttHelper.publishBoot(deviceName, client);
				deviceController.stop();
				SleepUtil.sleepMillis(5000);
				try {
					startService();
				} catch (IOException e) {
					MqttHelper.publishError(e,deviceName,client);
				}
			} else {
				LOG.info("Ação '" + action + "' inválida");
			}
		}

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}
}
