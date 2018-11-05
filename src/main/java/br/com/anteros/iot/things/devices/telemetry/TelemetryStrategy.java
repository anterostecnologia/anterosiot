package br.com.anteros.iot.things.devices.telemetry;

import javax.json.JsonObject;

import br.com.anteros.iot.Device;
import br.com.anteros.iot.collectors.TelemetryType;

public interface TelemetryStrategy {
	
	public JsonObject getTelemetryValue(Device device);
	
	public void setLastIntervalPublishing(long interval);
	
	public long getLastIntervalPublishing();
	
	public TelemetryType getTelemetryType();
	
	

}
