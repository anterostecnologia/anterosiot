package br.com.anteros.iot.app.listeners;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Actuable;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.controllers.AbstractDeviceController;
import br.com.anteros.iot.triggers.Trigger;

public interface AnterosIOTServiceListener {
	
	public void onBeforeStartService();
	
	public void onAfterStartService();
	
	public void onConnectingMqttServer();
	
	public void onErrorConnectingMqttServer(String error);

	public void onFireTrigger(Trigger source, Object value);
	
	public void onAddSubTypeNames(ObjectMapper mapper);

	public Set<Class<? extends Actuable>> getNewActuatorsToRegister();

	public void onBeforeBuildDeviceController();

	public void onAfterBuildDeviceController();
	
	public void onStopDeviceController();

	public void onStartCollectors(AbstractDeviceController abstractDeviceController);

	public void onStopCollectors(AbstractDeviceController abstractDeviceController);

	public void onStartDeviceController(Device device);

	public void onUpdateConfiguration();
}
