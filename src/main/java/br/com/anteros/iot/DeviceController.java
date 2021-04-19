package br.com.anteros.iot;

import br.com.anteros.client.mqttv3.MqttException;
import br.com.anteros.client.mqttv3.MqttPersistenceException;
import br.com.anteros.iot.actions.Action;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.support.AnterosMqttClient;

public interface DeviceController extends ThingController {

	public Device getDevice();
	
	public DeviceStatus getStatus();	
	
	public DeviceController addThings(Thing... things);
	
	public DeviceController removeThing(Thing thing);
	
	public void autoSubscribe();
	
	public void restartOS();
	
	public AnterosMqttClient getClientMqtt();
	
	public void setServiceListener(AnterosIOTServiceListener listener);
	
	public AnterosIOTServiceListener getServiceListener();
	
	public void dispatchAction(Action action, String value);

	public void dispatchMessage(String topic, String message);
	
	public void publishError(Exception ex, String deviceName) throws MqttPersistenceException, MqttException;		
	
	public void publishBoot(String deviceName) throws MqttPersistenceException, MqttException;

	public void publishHeartBeat(String deviceName, String deviceType, String status, Boolean controllerRunning, String hostAddress, AnterosMqttClient client) throws MqttPersistenceException, MqttException;
}
