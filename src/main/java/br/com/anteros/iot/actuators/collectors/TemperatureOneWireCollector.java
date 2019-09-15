package br.com.anteros.iot.actuators.collectors;

import com.diozero.util.SleepUtil;
import com.pi4j.component.temperature.TemperatureChangeEvent;
import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.w1.W1Master;
import com.pi4j.temperature.TemperatureScale;

import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.things.sensors.TemperatureSensorOneWire;


public class TemperatureOneWireCollector extends Collector implements Runnable {

	protected W1Master w1 = new W1Master();
	protected Boolean running = false;
	protected Thread thread;
	protected Double oldTemperature;
	protected Double newTemperature;
	private static final Logger LOG = LoggerProvider.getInstance().getLogger(TemperatureOneWireCollector.class.getName());

	public TemperatureOneWireCollector(CollectorListener listener, Thing thing) {
		super(listener, thing);
	}

	public TemperatureOneWireCollector() {
	}

	@Override
	public boolean isSupportedThing(Thing thing) {
		return thing instanceof TemperatureSensorOneWire;
	}

	@Override
	public void startCollect() {
		Assert.notNull(listener);
		if (thing instanceof TemperatureSensorOneWire) {
			this.running = true;
			thread = new Thread(this);
			thread.setName("Coletor temperature one wire");
			thread.start();
		}
	}

	@Override
	public void stopCollect() {
		this.running = false;
	}

	@Override
	public void run() {
		LOG.info("Iniciando coletor temperatura one wire...");
		while (running) {
			TemperatureSensorOneWire oneWire = (TemperatureSensorOneWire) thing;
			for (TemperatureSensor device : w1.getDevices(TemperatureSensor.class)) {
				String name = device.getName();
				name = name.replace("\n", "");
				if (name.equals(oneWire.getSensorId())) {
					
					double temperature = device.getTemperature(oneWire.getTemperatureScale());
					if (newTemperature==null || temperature != newTemperature.doubleValue()) {
						oldTemperature = newTemperature;
						newTemperature = Double.valueOf(temperature);
						
						if (listener != null) {
							listener.onCollect(TemperatureResult.of(oldTemperature, newTemperature), thing);
						}

					}
					
				}
			}
			SleepUtil.sleepMillis(500);
		}
	}

}
