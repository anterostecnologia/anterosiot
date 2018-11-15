package br.com.anteros.iot.processors;

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

import br.com.anteros.iot.Device;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.support.MqttHelper;

public class SimpleProcessorManager implements ProcessorManager, ProcessorListener {

	private MqttClient mqttClient;
	private Thread thread;
	private Boolean running = false;
	private List<Processor<?>> registeredProcessors = new ArrayList<>();
	private MqttClient clientProcessor;
	private ObjectMapper mapper = new ObjectMapper();
	private Map<String, Thing> subscribedTopics = new HashMap<>();
	private Device device;
	private ThreadPoolTaskExecutor processorExecutor;

	protected SimpleProcessorManager(Device device, MqttClient mqttClient, List<Processor<?>> processors) {
		this.thread = new Thread(this);
		this.registeredProcessors = processors;
		this.mqttClient = mqttClient;
		this.device = device;

		processorExecutor = new ThreadPoolTaskExecutor();
		processorExecutor.setCorePoolSize(10);
		processorExecutor.setMaxPoolSize(10);
		processorExecutor.setWaitForTasksToCompleteOnShutdown(false);
		processorExecutor.initialize();
	}

	public static SimpleProcessorManager of(Device device, MqttClient mqttClient, List<Processor<?>> processors) {
		return new SimpleProcessorManager(device, mqttClient, processors);
	}

	public void stop() {
		if (running) {
			this.running = false;
		}
	}

	public void start() {

		try {
			clientProcessor = MqttHelper.createAndConnectMqttClient(mqttClient.getServerURI(),
					device.getThingID() + "_processor", "", "", true, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.autoSubscribe();
		this.clientProcessor.setCallback(this);

		if (!running && !registeredProcessors.isEmpty()) {
			this.thread.start();
		}
	}

	@Override
	public void run() {
		running = true;
		boolean first = true;

		while (running) {
			if (first) {
				System.out.println("Processador de dados rodando...");
				first = false;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Parando processador de dados.");

		processorExecutor.shutdown();
		
		while (processorExecutor.getActiveCount() > 0) {
		}
		System.out.println("THREADS ATIVAS > " + processorExecutor.getActiveCount());
		
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
					System.out.println("JSon não é do tipo " + processor.getCollectResult().getSimpleName());
				}

				if (result != null && thing != null) {
//					System.out.println("=> Mensagem recebida: \"" + message.toString() + "\" no tópico \""
//							+ ((PlantItem) thing).getPath() + "\" para instancia \"" + thing.getThingID() + "\"");

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

		for (Processor<?> processor : registeredProcessors) {

			if (processor.getThing() instanceof PlantItem) {
				String topic = ((PlantItem) processor.getThing()).getPath();
				filter.add(topic);
				subscribedTopics.put(topic, processor.getThing());
			}
		}

		try {
			System.out.println(Arrays.toString(filter.toArray(new String[] {})));
			this.clientProcessor.subscribe(filter.toArray(new String[] {}));
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
