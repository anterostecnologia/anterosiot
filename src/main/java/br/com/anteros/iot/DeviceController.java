package br.com.anteros.iot;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;

public interface DeviceController extends ThingController {

	public DeviceStatus getStatus();	
	
	public DeviceController addThings(Thing... things);
	
	public DeviceController removeThing(Thing thing);
	
	public void autoSubscribe();
	
	public void restartOS();
	
	public MqttAsyncClient getClientMqtt();
	
	public void setServiceListener(AnterosIOTServiceListener listener);
	
	public AnterosIOTServiceListener getServiceListener();
	
	public void dispatchAction(Action action, String value);

	public void dispatchMessage(String topic, String message);
}
