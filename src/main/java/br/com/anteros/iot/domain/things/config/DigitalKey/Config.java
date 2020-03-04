package br.com.anteros.iot.domain.things.config.DigitalKey;

import br.com.anteros.iot.domain.things.config.AbstractConfig;
import br.com.anteros.iot.domain.things.config.General;
import br.com.anteros.iot.domain.things.config.Mqtt;
import br.com.anteros.iot.domain.things.config.NTP;
import br.com.anteros.iot.domain.things.config.Network;

public class Config extends AbstractConfig {
	
	private Hardware hardware;

	public Config() {
		
	}
	
	public Config(String command, Network network, Hardware hardware, General general, Mqtt mqtt, NTP ntp) {
		super(command, network, general, mqtt, ntp);
		this.hardware = hardware;
	}

	public Hardware getHardware() {
		return hardware;
	}

	public void setHardware(Hardware hardware) {
		this.hardware = hardware;
	}

}
