package br.com.anteros.iot.domain.things.config;

import br.com.anteros.iot.domain.things.config.General;
import br.com.anteros.iot.domain.things.config.Mqtt;
import br.com.anteros.iot.domain.things.config.NTP;
import br.com.anteros.iot.domain.things.config.Network;

public abstract class  AbstractConfig {
	
	private String command;
	
	private Network network;
	
	private General general;
	
	private Mqtt mqtt;
	
	private NTP ntp;

	public AbstractConfig() {
		
	}
	
	public AbstractConfig(String command, Network network, General general, Mqtt mqtt, NTP ntp) {
		this.command = command;
		this.network = network;
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
