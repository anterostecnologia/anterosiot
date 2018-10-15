package br.com.anteros.iot;

public interface DeviceController extends ThingController {

	public DeviceStatus getStatus();	
	
	public DeviceController addThings(Thing... things);
	
	public DeviceController removeThing(Thing thing);
	
	public void autoSubscribe();
	
	public void restartOS();


}
