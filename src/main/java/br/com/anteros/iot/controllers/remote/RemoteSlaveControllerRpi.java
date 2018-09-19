package br.com.anteros.iot.controllers.remote;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.MasterDeviceController;
import br.com.anteros.iot.RemoteSlaveDeviceController;
import br.com.anteros.iot.controllers.SlaveControllerRPi;

public class RemoteSlaveControllerRpi extends SlaveControllerRPi implements RemoteSlaveDeviceController {

	public RemoteSlaveControllerRpi(MasterDeviceController master, Device device) {
		super(master, device);
	}

	@Override
	public void stop() {
		
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
	}
	
	
	public static class Builder {

		private Device device;
		private MasterDeviceController master;

		public static Builder create() {
			return new Builder();
		}

		public Builder device(Device device) {
			this.device = device;
			return this;
		}
		
		public Builder master(MasterDeviceController master) {
			this.master = master;
			return this;
		}

		public RemoteSlaveDeviceController build() {
			Assert.notNull(device, "Informe o dispositivo remoto para o controlador slave.");
			Assert.notNull(master, "Informe o dispositivo master para o controlador slave.");
			return RemoteSlaveControllerRpi.of(master, device);
		}


	}


	public static RemoteSlaveDeviceController of(MasterDeviceController master, Device device) {
		return new RemoteSlaveControllerRpi(master, device);
	}

}
