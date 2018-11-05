package br.com.anteros.iot.things.devices.telemetry;

import java.util.HashMap;
import java.util.LinkedHashMap;

import br.com.anteros.iot.Device;

public class TelemetryConfiguration {
	
	private Device device;
	private HashMap<TelemetryStrategy, Integer> strategies = new LinkedHashMap<>();

	private TelemetryConfiguration(Device device) {
		this.device = device;
	}
	
	public TelemetryConfiguration addStrategy(TelemetryStrategy strategy, Integer intervalPublishing) {
		this.strategies.put(strategy, intervalPublishing);
		return this;
	}

	public Device getDevice() {
		return device;
	}
	
	public static TelemetryConfiguration of(Device device) {
		return new TelemetryConfiguration(device);
	}

	public boolean hasTelemetries() {
		return strategies.size()>0;
	}

	public HashMap<TelemetryStrategy, Integer> getStrategies() {
		return strategies;
	}
}
