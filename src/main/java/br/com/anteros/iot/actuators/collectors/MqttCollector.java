package br.com.anteros.iot.actuators.collectors;




import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.support.AnterosMqttClient;

public abstract class MqttCollector extends Collector {

	public MqttCollector() {
		super();
	}

	public MqttCollector(CollectorListener listener, Thing thing) {
		super(listener, thing);
	}

	protected abstract Collector setAnterosMqttClient(AnterosMqttClient clientCollector);

}
