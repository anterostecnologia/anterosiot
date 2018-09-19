package br.com.anteros.iot;

public interface DeviceController extends ThingController {

	public DeviceStatus getStatus();	
	
	public Actuator discoverActuatorToThing(Thing thing);
	
	public void registerActuator(Actuator actuator);
	
	public void unregisterActuator(Actuator actuator);
	
	public DeviceController addThings(Thing... things);
	
	public DeviceController removeThing(Thing thing);


}
