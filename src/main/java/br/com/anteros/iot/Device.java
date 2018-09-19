package br.com.anteros.iot;

import br.com.anteros.iot.things.devices.IpAddress;

public interface Device extends Thing {

	public IpAddress getIpAddress();
	
}
