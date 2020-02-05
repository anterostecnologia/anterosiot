package br.com.anteros.iot.actuators.collectors;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.support.Pi4JHelper;
import br.com.anteros.iot.things.sensors.PresenceDetectorSensor;

public class PresenceDetectorCollector extends Collector implements GpioPinListenerDigital {

	private GpioPinDigitalInput digitalInputPin;
	private boolean running;

	public PresenceDetectorCollector(CollectorListener listener, Thing thing) {
		super(listener, thing);

	}

	public PresenceDetectorCollector() {
	}

	@Override
	public boolean isSupportedThing(Thing thing) {
		return thing instanceof PresenceDetectorSensor;
	}

	@Override
	public void startCollect() {
		PresenceDetectorSensor presenceDetector = (PresenceDetectorSensor) thing;
		GpioController gpio = Pi4JHelper.getGpioController();
		digitalInputPin = Pi4JHelper.getDigitalInputPin(gpio, presenceDetector.getPin());
		digitalInputPin.setPullResistance(PinPullResistance.PULL_DOWN);
		digitalInputPin.addListener(this);
		running = true;

	}

	@Override
	public void stopCollect() {
		if (digitalInputPin != null) {
			digitalInputPin.removeListener(this);
		}
		running = false;
	}

	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		if (listener != null) {
			listener.onCollect(new SimpleResult(Boolean.valueOf(event.getState().isHigh())), thing);
		}
	}

	@Override
	public boolean isRunning() {
		return running ? true : false;
	}

}
