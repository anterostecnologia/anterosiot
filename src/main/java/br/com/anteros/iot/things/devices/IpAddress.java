package br.com.anteros.iot.things.devices;

public class IpAddress {

	private String ip;
	
	public IpAddress() {
		
	}

	private IpAddress(String ip) {
		this.ip = ip;
	}
	
	public String getIp() {
		return ip;
	}
	
	public static IpAddress of(String ip) {
		return new IpAddress(ip);
	}

}
