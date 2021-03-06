package br.com.anteros.iot;

import br.com.anteros.iot.things.devices.IpAddress;
import br.com.anteros.iot.things.devices.telemetry.TelemetryStrategy;

public interface Device extends Thing {
	
	public String getHostnameACL();
	
	public IpAddress getIpAddress();
	
	public Device setIpAddress(IpAddress ipAddress);
	
	public String getTopicError();
	
	public TelemetryStrategy[] getTelemetries();

	public boolean hasTelemetries();
	
	public TelemetryStrategy[] getTelemetriesByInterval(long ellapsedTime);

	
}
