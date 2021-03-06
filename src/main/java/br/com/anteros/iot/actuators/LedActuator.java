package br.com.anteros.iot.actuators;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

import br.com.anteros.iot.IOTContext;
import br.com.anteros.iot.StatusListener;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import br.com.anteros.iot.Actuator;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.actuators.collectors.CollectorListener;
import br.com.anteros.iot.support.Pi4JHelper;
import br.com.anteros.iot.things.Led;
import br.com.anteros.iot.things.Semaphore;
import br.com.anteros.iot.things.parts.LedSemaphore;

public class LedActuator implements Actuator<Boolean> {

	public static String ON = "on";
	public static String OFF = "off";
	public static String TOGGLE = "toggle";
	protected Map<Thing, GpioPinDigitalOutput> pins = new HashMap<>();

	public LedActuator() {

	}

	public boolean isSupportedThing(Thing thing) {
		return thing instanceof Semaphore;
	}

	public Boolean executeAction(IOTContext context) {
		JsonObject receivedPayload = context.getReceivedPayload();
		Thing thing = context.getThing();
		StatusListener listener = context.getListener();
		String action = receivedPayload.getString("action");
		if (thing instanceof LedSemaphore || thing instanceof Led) {
			if (action.equals(ON)) {
				GpioController gpio = Pi4JHelper.getGpioController();
				final GpioPinDigitalOutput pin = getOutputPinFromThing(gpio, thing);
				gpio.high(pin);
				return true;
			} else if (action.equals(OFF)) {
				GpioController gpio = Pi4JHelper.getGpioController();
				final GpioPinDigitalOutput pin = getOutputPinFromThing(gpio, thing);
				gpio.low(pin);
				return true;
			} else if (action.equals(TOGGLE)) {
				GpioController gpio = Pi4JHelper.getGpioController();
				final GpioPinDigitalOutput pin = getOutputPinFromThing(gpio, thing);
				if (pin.isHigh())
					gpio.low(pin);
				else
					gpio.high(pin);
				return true;
			}
		}
		return false;
	}

	protected GpioPinDigitalOutput getOutputPinFromThing(GpioController gpio, Thing thing) {
		if (!pins.containsKey(thing)) {
			int pinNumber = ((LedSemaphore) thing).getPin();
			GpioPinDigitalOutput result = Pi4JHelper.getDigitalOutputPin(gpio, pinNumber, "LED");
			pins.put(thing, result);
		}
		return pins.get(thing);
	}

	@Override
	public String toString() {
		return "LedActuator []";
	}
}
