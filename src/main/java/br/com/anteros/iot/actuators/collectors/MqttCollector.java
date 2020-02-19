package br.com.anteros.iot.actuators.collectors;

import br.com.anteros.client.mqttv3.MqttAsyncClient;
import br.com.anteros.client.mqttv3.MqttClient;

import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Thing;

public abstract class MqttCollector extends Collector {

	public MqttCollector() {
		super();
	}

	public MqttCollector(CollectorListener listener, Thing thing) {
		super(listener, thing);
	}

	protected abstract Collector setMqttClient(MqttAsyncClient clientCollector);

}
