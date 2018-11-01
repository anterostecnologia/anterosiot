package br.com.anteros.iot.collectors;

import org.eclipse.paho.client.mqttv3.MqttClient;

import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Thing;

public abstract class MqttCollector extends Collector {

	public MqttCollector() {
		super();
	}

	public MqttCollector(CollectorListener listener, Thing thing) {
		super(listener, thing);
	}

	public abstract Collector setMqttClient(MqttClient client);

}
