package br.com.anteros.iot.collectors;

import javax.json.JsonObject;

import org.apache.commons.lang3.time.StopWatch;

import com.diozero.util.SleepUtil;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.things.devices.telemetry.TelemetryStrategy;

public class DeviceSystemInfoCollector extends Collector implements Runnable {

	protected Boolean running = false;
	protected Thread thread;

	public DeviceSystemInfoCollector(CollectorListener listener, Thing thing) {
		super(listener, thing);
	}

	public DeviceSystemInfoCollector() {
	}

	@Override
	public boolean isSupportedThing(Thing thing) {
		return thing instanceof Device;
	}

	@Override
	public void startCollect() {
		Assert.notNull(listener);
		if (thing instanceof Device) {
			this.running = true;
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void stopCollect() {
		this.running = false;
	}

	@Override
	public void run() {
		Device device = (Device) thing;
		System.out.println("Iniciando coletor informações telemetria device " + thing.getThingID());
		StopWatch watch = new StopWatch();
		watch.start();
		while (running) {
			if (device.hasTelemetries()) {
				long time = watch.getTime();
				TelemetryStrategy[] telemetriesByInterval = device.getTelemetriesByInterval(time);
				for (TelemetryStrategy strategy : telemetriesByInterval) {
					JsonObject telemetryValue = strategy.getTelemetryValue(device);
					listener.onCollect(new TelemetryResult(telemetryValue, strategy.getTelemetryType()), device);
					strategy.setLastIntervalPublishing(time);
				}
			}
			SleepUtil.sleepMillis(10);
		}
	}

}
