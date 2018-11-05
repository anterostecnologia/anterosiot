package br.com.anteros.iot.things.devices.telemetry;

import javax.json.JsonObject;

import br.com.anteros.iot.Device;
import br.com.anteros.iot.collectors.TelemetryType;
import br.com.anteros.iot.support.SystemInfoHelper;

public class PlatformTelemetryStrategy implements TelemetryStrategy{

	private long lastIntervalPublishing;

	@Override
	public JsonObject getTelemetryValue(Device device) {
		return SystemInfoHelper.getPlatformJsonRaspberryPi();
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
		return TelemetryType.PLATFORM;
	}	

}
