package br.com.anteros.iot.actuators;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.controllers.SlaveControllerRPi;

public class ControllerActuator  {
	
	public ControllerActuator() {
	}
	
	public boolean isSupportedThing(Thing thing) {
		return false;
	}

	public void executeAction(String action, Thing thing) {
		
		AbstractDeviceController deviceController = new SlaveControllerRPi(null, null);
		
		switch (action) {
		case "stop":
			deviceController.stop();
			break;
		case "start":
			deviceController.start();
			break;
		case "resume":
			deviceController.resume();
			break;
		case "pause":
			deviceController.pause();
			break;
		case "restart os":
			deviceController.restartOS();
			break;
		}	
	}

}
