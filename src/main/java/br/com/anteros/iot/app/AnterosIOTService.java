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
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import com.diozero.util.SleepUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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

	private String deviceName;
	private File fileConfig;
	private AbstractDeviceController deviceController;
	private String hostMqtt;
	private String port;
	private String username;
	private String password;
	private MqttClient client;
	private AnterosIOTServiceListener serviceListener;
	private InputStream streamConfig;
	private Set<Class<? extends Actuable>> actuators = new HashSet<>();

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

		new Thread(new AnterosIOTService(deviceName, hostMqtt, port, username, password, configFile, null, null, null))
				.start();
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
		
		serviceListener.onConnectingMqttServer();

		String broker = "tcp://" + hostMqtt + ":" + (port == null ? 1883 : port);
		String clientId = deviceName + "_guardian";

		System.out.println("Conectando servidor broker MQTT...");

		try {
			client = MqttHelper.createAndConnectMqttClient(broker, clientId, username, password, true, true);
		} catch (MqttException e1) {
			serviceListener.onErrorConnectingMqttServer("Ocorreu uma falha ao criar um cliente mqtt. O Sistema não continuará a inicialização.");
			e1.printStackTrace();
		}

		if ((fileConfig != null || streamConfig != null) && client != null && client.isConnected()) {
			try {
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
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (deviceController != null) {
			serviceListener.onStartDeviceController();
			deviceController.start();
		}

		while (true) {
			SleepUtil.sleepMillis(2000);
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
	public void messageArrived(String topic, MqttMessage message) throws Exception {
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
	}
}
