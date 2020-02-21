package br.com.anteros.iot;

import java.util.Set;

import br.com.anteros.client.mqttv3.MqttCallback;

public interface MasterDeviceController extends DeviceController, MqttCallback, Runnable{
	
	public Set<DeviceController> controllers();

	public void stopAllSlaves();
	
	public MasterDeviceController addChildDeviceController(DeviceController child);
	
	public MasterDeviceController removeChildDeviceController(DeviceController child);	


}
