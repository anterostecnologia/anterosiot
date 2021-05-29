package br.com.anteros.iot.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import br.com.anteros.client.mqttv3.*;
import com.diozero.util.SleepUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.iot.Actuator;
import br.com.anteros.iot.Actuators;
import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.DeviceStatus;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.actions.Action;
import br.com.anteros.iot.actuators.collectors.CollectorManager;
import br.com.anteros.iot.actuators.collectors.SimpleCollectorManager;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.plant.Place;
import br.com.anteros.iot.plant.Plant;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.support.AnterosMqttClient;
import br.com.anteros.iot.support.MqttHelper;
import br.com.anteros.iot.things.devices.IpAddress;

public abstract class AbstractDeviceController
		implements DeviceController, MqttCallback, MqttCallbackExtended, Runnable {

	private PlantItemNode node;
	protected Device device;
	protected Set<Thing> things = new HashSet<Thing>();
	protected AnterosMqttClient clientMqtt;
	protected String username;
	protected String password;
	protected Boolean running = false;
	protected Boolean paused = false;
	protected Thread thread;
	protected ObjectMapper mapper = new ObjectMapper();
	protected Actuators actuators;
	protected AnterosIOTServiceListener serviceListener;
	protected Map<String, Thing> subscribedTopics = new HashMap<>();

	protected boolean sendMsgServiceStarted = false;
	private boolean alreadyConnectedOnce = false;

	private static final Logger LOG = LoggerProvider.getInstance().getLogger(AbstractDeviceController.class.getName());
	private SimpleCollectorManager collectorManager;

	public AbstractDeviceController() {

	}

	public AbstractDeviceController(AnterosMqttClient remoteClientMqtt, DeviceNode node, Actuators actuators,
			AnterosIOTServiceListener serviceListener, String username, String password) {
		this.clientMqtt = remoteClientMqtt;
		this.username = username;
		this.password = password;
		this.clientMqtt.setCallback(this);
		this.thread = new Thread(this);
		this.thread.setName("Device controller");
		this.actuators = actuators;
		this.serviceListener = serviceListener;
	}

	protected AbstractDeviceController(Device device, Actuators actuators) {
		this.device = device;
		this.device.setDeviceController(this);
		this.thread = new Thread(this);
		this.thread.setName("Device controller");
		this.actuators = actuators;
	}

	protected AbstractDeviceController(AnterosMqttClient clientMqtt, Device device, Actuators actuators, String username,
			String password) {
		this(device, actuators);
		this.clientMqtt = clientMqtt;
		this.username = username;
		this.password = password;
		this.clientMqtt.setCallback(this);
		this.actuators = actuators;
	}

	public Thing getThing() {
		return device;
	}

	public DeviceStatus getStatus() {
		return null;
	}

	public void beforeStart() {

	}

	public void beforeStop() {

	}

	public void beforeRestart() {

	}

	public void afterStart() {

	}

	public void afterRestart() {

	}

	@Override
	public void beforePause() {

	}

	@Override
	public void afterResume() {

	}

	public void stop() {
		this.sendMsgServiceStarted = false;
		if (running) {
			this.beforeStop();
			stopCollectoresAndMqtt(collectorManager);
			this.running = false;
		}

	}

	public void start() {
		if (!running) {
			this.beforeStart();
			try {
				this.thread.start();
				this.afterStart();
			} catch (Exception ex){
				ex.printStackTrace();
			}

		}
	}

	public void pause() {
		if (!paused) {
			this.beforePause();
			this.paused = true;
		}
	}

	public void resume() {
		if (paused) {
			this.paused = false;
			this.afterResume();
		}
	}

	public boolean isAvailable() {
		return !paused && running;
	}

	public String getThingID() {
		return device.getThingID();
	}

	public DeviceController addThings(Thing... things) {
		for (Thing thing : things) {
			thing.setDeviceController(this);
		}
		this.things.addAll(Arrays.asList(things));
		return this;
	}

	public DeviceController removeThing(Thing thing) {
		this.things.remove(thing);
		return this;
	}

	public Thing getThingById(String thingId) {
		for (Thing thing : things) {
			if (thing.getThingID() == null){
				throw new IllegalArgumentException("O Id da coisa "+thing+" não foi informado.");
			}
			if (thingId.equals(thing.getThingID())) {
				return thing;
			}
		}
		return null;
	}

	public Thing getThingByTopic(String topic) {
		for (String tpc : subscribedTopics.keySet()) {
			if (tpc.equals(topic)) {
				return subscribedTopics.get(tpc);
			}
		}
		return null;
	}

	public void run() {
		this.running = true;
		this.sendMsgServiceStarted = true;
		LOG.info("Iniciando device controller " + this.getThingID());

		serviceListener.onStartCollectors(this);

		LOG.info("Criando coletores de dados...");
		collectorManager = SimpleCollectorManager.of(clientMqtt, things.toArray(new Thing[] {}),
				actuators, device, username, password);
		collectorManager.start();

		boolean first = true;

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {

		}
		notifyService();

		while (running) {
			if (first) {
				LOG.info("Device controller " + this.getThingID() + " rodando...");
				first = false;
			}
			if (this.clientMqtt != null && this.clientMqtt.isConnected()) {
				SleepUtil.sleepMillis(200);
			} else if (this.clientMqtt != null && alreadyConnectedOnce ) {
				try {
					this.clientMqtt.reconnect();
				} catch (MqttException e) {
					if (e.getReasonCode() == MqttException.REASON_CODE_CONNECT_IN_PROGRESS || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING) {
						SleepUtil.sleepMillis(2000);
					} else {
						e.printStackTrace();
					}
				}
			}
		}

		stopCollectoresAndMqtt(collectorManager);

	}

	private void stopCollectoresAndMqtt(CollectorManager collectorManager) {
		if (collectorManager.isRunning()) {
			serviceListener.onStopCollectors(this);
			LOG.info("Parando Coletores de dados...");
			collectorManager.stop();

			LOG.info("Parando device controller " + this.getThingID());

			try {
				unSubscribe();
				if (clientMqtt.isConnected()) {
					clientMqtt.disconnect();
				}
			} catch (MqttException e) {
				LOG.error("Ocorreu uma falha ao deconectar: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void restartOS() {
		Process p;
		try {
			p = Runtime.getRuntime().exec("sudo shutdown -h now");
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void connectionLost(Throwable cause) {
		try {
			this.clientMqtt.reconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {

		LOG.info("Recebeu mensagem device controller "+new String(message.getPayload()));

		byte[] payload = message.getPayload();
		JsonObject receivedPayload = null;
		try {
			InputStream stream = new ByteArrayInputStream(payload);

			JsonReader jsonReader = Json.createReader(stream);
			receivedPayload = jsonReader.readObject();
			jsonReader.close();

		} catch (Exception e) {
			LOG.error("Mensagem recebida não é do tipo JSON, por favor verifique.");
		}

		Thing thing = this.getThingByTopic(topic);

		if (receivedPayload != null && thing != null && receivedPayload.containsKey("action")) {
			LOG.info("=> Mensagem recebida: \"" + message.toString() + "\" no tópico \"" + topic.toString()
					+ "\" para instancia \"" + getThingID() + "\"");

			LOG.info(receivedPayload);
			LOG.info("Coisa " + thing + " responsável pelo tópico " + topic);

			Part part = null;
			if (thing instanceof Part) {
				part = (Part) thing;
				thing = part.getOwner();
			}
			LOG.info("Despachando ação para coisa " + thing + " parte " + part);

			Thread thread = new Thread(new DispatcherAction(Action.of(thing, part, receivedPayload,null), null, clientMqtt.getServerURI(), clientMqtt.getOptions().getUserName(),
					new String(clientMqtt.getOptions().getPassword())));
			thread.setName("Atuador coisa "+thing.getThingID());
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
		}

	}

	public abstract void deliveryComplete(IMqttDeliveryToken token);

	protected abstract Device doCreateDevice(String deviceName, IpAddress ipAddress, String description,
			String topicError, Integer intervalPublishingTelemetry, String hostnameACL);



	class DispatcherAction implements Runnable {

		private final String userName;
		private final String password;
		private final String uri;
		private Action action;
		private String value;
		private  AnterosMqttClient clientMqtt;

		public DispatcherAction(Action action, String value, String uri, String userName, String password) {
			this.action = action;
			this.value = value;
			this.userName = userName;
			this.password = password;
			this.uri = uri;
		}

		public void dispatchMessage(String topic, String message) {
			if ((StringUtils.isNotEmpty(topic) || StringUtils.isNotEmpty(message))) {
				try {
					MqttMessage msg = new MqttMessage(message.getBytes());
					msg.setQos(1);
					this.clientMqtt.publish(topic, msg);
					LOG.info("Publicando mensagem para " + topic);
					LOG.info("Mensagem: " + message);
				} catch (MqttPersistenceException e) {
					e.printStackTrace();
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {
			Instant start = Instant.now();
			Actuator<?> actuator=null;
			try {
				if (action.getThing() != null) {
					actuator = actuators
							.discoverActuatorToThing(action.getPart() != null ? action.getPart() : action.getThing());
					if (actuator != null) {
						if (action.canExecute()) {
							try {
								LOG.info("Executando ação " + action);
								if (actuator instanceof Collector) {
									((Collector) actuator).getListener();
									actuator.executeAction(action.getReceivedPayload(),
											action.getPart() != null ? action.getPart() : action.getThing());
								} else {
									actuator.executeAction(action.getReceivedPayload(),
											action.getPart() != null ? action.getPart() : action.getThing());
								}
							} catch (Exception e) {
								LOG.error("Ocorreu erro ao executar ação " + action);
								JsonObject jsonMessage = Json.createObjectBuilder()
										.add("thing",
												action.getPart().getThingID() != null ? action.getPart().getThingID()
														: action.getThing().getThingID())
										.add("message", "" + e.getMessage()).build();
								try {
									LOG.error("Enviando mensagem de erro para " + device.getTopicError());
									LOG.error("Mensagem de Erro: " + e.getMessage());
									this.clientMqtt = MqttHelper.createMqttClient(uri,AnterosMqttClient.generateClientId(),userName, password,true,true);
									this.clientMqtt.connect();
									this.clientMqtt.publish(device.getTopicError(), jsonMessage.toString().getBytes(), 0,
											false);
								} catch (MqttException e1) {
									LOG.error(e1.getMessage());
								}
							}
						}
					}
				}

				if ((StringUtils.isNotEmpty(action.getMessage()) || StringUtils.isNotEmpty(value))
						&& (action.getTopics() != null)) {
					if (this.clientMqtt==null){
						this.clientMqtt = MqttHelper.createMqttClient(uri,AnterosMqttClient.generateClientId(),userName, password,true,true);
						this.clientMqtt.connect();
					}
					for (String topic : action.getTopics()) {
						this.dispatchMessage(topic, StringUtils.isNotEmpty(value) ? value : action.getMessage());
					}
				}
			} catch (MqttSecurityException e) {
				e.printStackTrace();
			} catch (MqttException e) {
				e.printStackTrace();
			} finally {
				Instant finish = Instant.now();
				float timeElapsed = Duration.between(start, finish).toMillis() / 1000F;
				if (actuator!=null) {
					LOG.info("Executou ACAO " + (action.getAction()==null?"<NÃO INFORMADA>":action.getAction()) + " no atuador " + actuator.getClass().getSimpleName()+" inicio em "+start+" fim "+finish+" tempo "+timeElapsed+" segundos.");
				}

				if (clientMqtt != null) {
					try {
						clientMqtt.disconnect();
					} catch (MqttException e) {
					}
				}
			}
		}
	}

	public void dispatchMessage(String topic, String message) {
		if ((StringUtils.isNotEmpty(topic) || StringUtils.isNotEmpty(message))) {
			try {

				MqttMessage msg = new MqttMessage(message.getBytes());
				msg.setQos(1);
				this.clientMqtt.publish(topic, msg);
				LOG.info("Publicando mensagem para " + topic);
				LOG.info("Mensagem: " + message);
			} catch (MqttPersistenceException e) {
				e.printStackTrace();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}


	public void loadConfiguration(DeviceNode itemNode, Plant plant) {
		Place place = (Place) plant.getItemByName(itemNode.getItemNodeOwner().getItemName());
		this.device = doCreateDevice(itemNode.getItemName(), itemNode.getIpAddress(), itemNode.getDescription(),
				itemNode.getTopicError(), itemNode.getIntervalPublishingTelemetry(), itemNode.getHostnameACL());
		if (!(this.device instanceof PlantItem)) {
			throw new DeviceException("O device " + itemNode.getItemName() + " não é um item da planta.");
		}
		place.addItems((PlantItem) device);
	}

	@Override
	public void autoSubscribe() {

		LOG.info("Subscrevendo nos tópicos: ");
		List<String> filter = new ArrayList<>();

		for (Thing thing : things) {
			if (thing instanceof PlantItem) {
				String topic = ((PlantItem) thing).getPath();
				filter.add(topic);
				subscribedTopics.put(topic, thing);
				if (thing.getParts() != null) {
					for (Part part : thing.getParts()) {
						String topicPart = ((PlantItem) part).getPath();
						filter.add(topicPart);
						subscribedTopics.put(topicPart, part);
					}
				}
			}
		}

		if (this.getThing() instanceof PlantItem) {
			String topic = ((PlantItem) this.getThing()).getPath();
			filter.add(topic);
			subscribedTopics.put(topic, this.getThing());
		}

		try {
			String[] filterArray = filter.toArray(new String[] {});
			int[] qosArray = new int[filterArray.length];
			Arrays.fill(qosArray, 1);
			LOG.info(Arrays.toString(filter.toArray(new String[] {})));
			this.clientMqtt.subscribe(filterArray, qosArray);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void unSubscribe() {

		List<String> filter = new ArrayList<>();

		for (Map.Entry<String, Thing> entry : subscribedTopics.entrySet()) {
			filter.add(entry.getKey());
		}

		try {
			LOG.info("Removendo subscrição nos tópicos: ");
			LOG.info(Arrays.toString(filter.toArray(new String[] {})));
			if (this.clientMqtt.isConnected()) {
				this.clientMqtt.unsubscribe(filter.toArray(new String[] {}));
			}
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public AnterosMqttClient getClientMqtt() {
		return clientMqtt;
	}

	@Override
	public void setServiceListener(AnterosIOTServiceListener listener) {
		this.serviceListener = listener;
	}

	@Override
	public AnterosIOTServiceListener getServiceListener() {
		return serviceListener;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Set<Thing> getThings() {
		return things;
	}

	public void setThings(Set<Thing> things) {
		this.things = things;
	}

	public Boolean getRunning() {
		return running;
	}

	public void setRunning(Boolean running) {
		this.running = running;
	}

	public Boolean getPaused() {
		return paused;
	}

	public void setPaused(Boolean paused) {
		this.paused = paused;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public Actuators getActuators() {
		return actuators;
	}

	public void setActuators(Actuators actuators) {
		this.actuators = actuators;
	}

	public Map<String, Thing> getSubscribedTopics() {
		return subscribedTopics;
	}

	public void setSubscribedTopics(Map<String, Thing> subscribedTopics) {
		this.subscribedTopics = subscribedTopics;
	}

	public void setClientMqtt(AnterosMqttClient clientMqtt) {
		this.clientMqtt = clientMqtt;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		if (!alreadyConnectedOnce) {
			alreadyConnectedOnce = true;
		}
		/**
		 * Ouvindo mensagens MQTT para todas as coisas sendo contraladas e mais o
		 * próprio controlador
		 */
		this.autoSubscribe();
		notifyService();
	}

	private void notifyService() {
		if (!this.clientMqtt.isConnected()) {
			try {
				this.clientMqtt.reconnect();
			} catch (MqttException e) {
				if (e.getReasonCode() == MqttException.REASON_CODE_CONNECT_IN_PROGRESS || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING) {
					SleepUtil.sleepMillis(2000);
				} else {
					e.printStackTrace();
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}

		if (this.sendMsgServiceStarted && this.clientMqtt.isConnected()) {
			LOG.info("Enviando notificação sobre serviço iniciado.");
			String msg = "{\"status\":\"running\", \"message\": \"Serviço iniciado com sucesso!\"}";
			MqttMessage message = new MqttMessage(msg.getBytes());
			try {
				sendMsgServiceStarted = false;
				this.clientMqtt.publish("/" + this.getDevice().getThingID() + "/notify", message);
			} catch (MqttPersistenceException e1) {
				e1.printStackTrace();
			} catch (MqttException e1) {
				e1.printStackTrace();
			}
		}
	}

	public PlantItemNode getNode() {
		return node;
	}

	public void setNode(PlantItemNode node) {
		this.node = node;
	}
	
	

	public void publishError(Exception ex, String deviceName) throws MqttPersistenceException, MqttException {		
		MqttHelper.publishError(ex, deviceName, clientMqtt);
	}
	
	public void publishBoot(String deviceName) throws MqttPersistenceException, MqttException {
		MqttHelper.publishBoot(deviceName, clientMqtt);
	}


	public void publishHeartBeat(String deviceName, String deviceType, String status, Boolean controllerRunning, String hostAddress, AnterosMqttClient client) throws MqttPersistenceException, MqttException {
		MqttHelper.publishHeartBeat(deviceName, deviceType, status, controllerRunning, hostAddress, clientMqtt);		
	}

}
