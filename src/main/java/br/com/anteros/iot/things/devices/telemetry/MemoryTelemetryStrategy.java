package br.com.anteros.iot.things.devices.telemetry;

import javax.json.JsonObject;

import br.com.anteros.iot.Device;
import br.com.anteros.iot.actuators.collectors.TelemetryType;
import br.com.anteros.iot.support.SystemInfoHelper;

public class MemoryTelemetryStrategy implements TelemetryStrategy {

	private long lastIntervalPublishing;

	@Override
	public JsonObject getTelemetryValue(Device device) {
		return SystemInfoHelper.getMemoryJsonRaspberryPi();
	}

	@Override
	public void setLastIntervalPublishing(long interval) {
		this.lastIntervalPublishing = interval;		
	}

	@Override
	public long getLastIntervalPublishing() {
		return lastIntervalPublishing;
	}	
	
	@Override
	public TelemetryType getTelemetryType() {
		return TelemetryType.MEMORY;
	}
	
	
}
