package br.com.anteros.iot.controllers.remote;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.RemoteMasterDeviceController;
import br.com.anteros.iot.controllers.MasterControllerRPi;

public class RemoteMasterControllerRPi extends MasterControllerRPi implements RemoteMasterDeviceController {

	public RemoteMasterControllerRPi(Device device) {
		super(device);
	}
	
	
	public static class Builder {

		private Device device;

		public static Builder create() {
			return new Builder();
		}

		public Builder device(Device device) {
			this.device = device;
			return this;
		}
		
		public RemoteMasterDeviceController build() {
			Assert.notNull(device, "Informe o dispositivo para o controlador master remoto.");
			return RemoteMasterControllerRPi.of(device);
		}

	}


	public static RemoteMasterControllerRPi of(Device device) {
		return new RemoteMasterControllerRPi(device);
	}
	

}
