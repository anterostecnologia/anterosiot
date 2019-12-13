package br.com.anteros.iot.domain.things.config;

public class Config {
	
	private String command;
	
	private Network network;
	
	private Hardware hardware;
	
	private General general;
	
	private Mqtt mqtt;
	
	private NTP ntp;

	public Config() {
		
	}
	
	public Config(String command, Network network, Hardware hardware, General general, Mqtt mqtt, NTP ntp) {
		this.command = command;
		this.network = network;
		this.hardware = hardware;
		this.general = general;
		this.mqtt = mqtt;
		this.ntp = ntp;
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public Hardware getHardware() {
		return hardware;
	}

	public void setHardware(Hardware hardware) {
		this.hardware = hardware;
	}

	public General getGeneral() {
		return general;
	}

	public void setGeneral(General general) {
		this.general = general;
	}

	public Mqtt getMqtt() {
		return mqtt;
	}

	public void setMqtt(Mqtt mqtt) {
		this.mqtt = mqtt;
	}

	public NTP getNtp() {
		return ntp;
	}

	public void setNtp(NTP ntp) {
		this.ntp = ntp;
	}
}
