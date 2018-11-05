package br.com.anteros.iot.things.devices.telemetry;

import javax.json.JsonObject;

import br.com.anteros.iot.Device;
import br.com.anteros.iot.collectors.TelemetryType;
import br.com.anteros.iot.support.SystemInfoHelper;
import br.com.anteros.iot.things.devices.RaspberryPI;
import br.com.anteros.iot.things.devices.RaspberryPIZero;

public class AllSystemInfoTelemetry implements TelemetryStrategy {

	private long lastIntervalPublishing;

	@Override
	public JsonObject getTelemetryValue(Device device) {
		if (device instanceof RaspberryPI || device instanceof RaspberryPIZero) {
			return SystemInfoHelper.getAllSystemInfoJsonRaspberryPi();
		}
		return null;
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
		return TelemetryType.ALL;
	}	

}
