package br.com.anteros.iot.actuators.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.core.utils.ArrayUtils;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.support.MqttHelper;

public class SimpleProcessorManager implements ProcessorManager, ProcessorListener {

	// private static final Logger logger =
	// LogManager.getLogger(SimpleProcessorManager.class);
	private static final Logger logger = LoggerProvider.getInstance().getLogger(SimpleProcessorManager.class.getName());

	private MqttClient mqttClient;
	private Thread thread;
	private Boolean running = false;
	private List<Processor<?>> registeredProcessors = new ArrayList<>();
	private MqttClient clientProcessor;
	private ObjectMapper mapper = new ObjectMapper();
	private Map<String, Thing> subscribedTopics = new HashMap<>();
	private ThreadPoolTaskExecutor processorExecutor;
	protected String username;
	protected String password;
	protected Device device;

	protected SimpleProcessorManager(MqttClient mqttClient, List<Processor<?>> processors, String username,
			String password, Device device) {
		this.thread = new Thread(this);
		this.registeredProcessors = processors;
		this.mqttClient = mqttClient;
		this.username = username;
		this.password = password;
		this.device = device;

		processorExecutor = new ThreadPoolTaskExecutor();
		processorExecutor.setCorePoolSize(10);
		processorExecutor.setMaxPoolSize(10);
		processorExecutor.setWaitForTasksToCompleteOnShutdown(false);
		processorExecutor.initialize();
	}

	public static SimpleProcessorManager of(MqttClient mqttClient, List<Processor<?>> processors, String username,
			String password, Device device) {
		return new SimpleProcessorManager(mqttClient, processors, username, password, device);
	}

	public void stop() {
		if (running) {
			this.running = false;
		}
	}

	public void start() {

		try {
			clientProcessor = MqttHelper.createAndConnectMqttClient(mqttClient.getServerURI(),
					SimpleProcessorManager.class.getSimpleName() + "_processor", username, password, true, true);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		if (registeredProcessors != null && !registeredProcessors.isEmpty()) {
			this.autoSubscribe();
			this.clientProcessor.setCallback(this);

			if (!running) {
				logger.debug("Iniciou simple processor.");
				this.thread.start();
			}
		}
	}

	@Override
	public void run() {
		running = true;
		boolean first = true;

		while (running) {
			if (first) {
				logger.debug("Processador de dados rodando...");
				first = false;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
		logger.debug("Parando processador de dados.");

		processorExecutor.shutdown();

		while (processorExecutor.getActiveCount() > 0) {
		}
		logger.debug("THREADS ATIVAS > " + processorExecutor.getActiveCount());

		try {
			if (clientProcessor.isConnected())
				clientProcessor.disconnect();

			for (Processor<?> processor : registeredProcessors) {
				if (processor instanceof MqttProcessor && ((MqttProcessor<?>) processor).getMqttClient() != null) {
					if (((MqttProcessor<?>) processor).getMqttClient().isConnected())
						((MqttProcessor<?>) processor).getMqttClient().disconnect();
				}
			}
		} catch (MqttException e) {
			System.out.println("Ocorreu uma falha ao parar o ProcessorManager : " + e.getMessage());
			e.printStackTrace();
		}

		this.thread.interrupt();
	}

	@Override
	public void connectionLost(Throwable cause) {
		cause.printStackTrace();
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		Thing thing = this.getThingByTopic(topic);
		execute(message, thing);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}

	@Override
	public void execute(MqttMessage message, Thing thing) {

		for (Processor processor : registeredProcessors) {

			if (processor.getThing().getThingID().equals(thing.getThingID())) {

				CollectResult result = null;

				try {
					byte[] payload = message.getPayload();
					Class<? extends CollectResult> collectResult = processor.getCollectResult();

					result = mapper.readValue(payload, collectResult);
				} catch (Exception e) {
					logger.error("JSon não é do tipo " + processor.getCollectResult().getSimpleName());
					return;
				}

				if (processor instanceof MqttProcessor && ((MqttProcessor) processor).getMqttClient() == null) {

					try {
						((MqttProcessor) processor).setMqttClient(MqttHelper.createAndConnectMqttClient(
								mqttClient.getServerURI(), null, username, password, true, true));
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}

				if (processor.getDevice() == null) {
					processor.setDevice(device);
				}

				if (result != null && thing != null) {
					processor.setResult(result);
					processorExecutor.submit(processor);
				}
			}
		}
	}

	public Thing getThingByTopic(String topic) {
		for (String tpc : subscribedTopics.keySet()) {
			if (tpc.equals(topic)) {
				return subscribedTopics.get(tpc);
			}
		}
		return null;
	}

	public void autoSubscribe() {
		List<String> filter = new ArrayList<>();
		List<Integer> filterQos = new ArrayList<>();

		for (Processor<?> processor : registeredProcessors) {

			if (processor.getThing() instanceof PlantItem) {
				String topic = ((PlantItem) processor.getThing()).getPath();
				filter.add(topic);
				filterQos.add(2);
				subscribedTopics.put(topic, processor.getThing());
			}
		}

		try {
			System.out.println(Arrays.toString(filter.toArray(new String[] {})));
			this.clientProcessor.subscribe(filter.toArray(new String[] {}),
					ArrayUtils.toPrimitive(filterQos.toArray(new Integer[] {})));
		} catch (MqttException e) {
			logger.error(e.getMessage());
		}
	}
}
