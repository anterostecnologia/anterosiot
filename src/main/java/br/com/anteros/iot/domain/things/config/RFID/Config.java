package br.com.anteros.iot.domain.things.config.RFID;

import br.com.anteros.iot.domain.things.config.AbstractConfig;
import br.com.anteros.iot.domain.things.config.General;
import br.com.anteros.iot.domain.things.config.Mqtt;
import br.com.anteros.iot.domain.things.config.NTP;
import br.com.anteros.iot.domain.things.config.Network;

public class Config extends AbstractConfig {
	
	private Hardware hardware;
	private Notification notification;

	public Config() {
		
	}
	
	public Config(String command, Network network, Hardware hardware, Notification notification, General general, Mqtt mqtt, NTP ntp) {
		super(command, network, general, mqtt, ntp);
		this.hardware = hardware;
		this.notification = notification;
	}

	public Hardware getHardware() {
		return hardware;
	}

	public void setHardware(Hardware hardware) {
		this.hardware = hardware;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

}
