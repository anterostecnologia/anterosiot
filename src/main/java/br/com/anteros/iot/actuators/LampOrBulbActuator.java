package br.com.anteros.iot.actuators;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import br.com.anteros.iot.Actuator;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.support.Pi4JHelper;
import br.com.anteros.iot.things.LampOrBulb;
import br.com.anteros.iot.triggers.TriggerType;

public class LampOrBulbActuator implements Actuator<Boolean> {

	public static String ON = "on";
	public static String OFF = "off";
	public static String TOGGLE = "toggle";
	protected Map<Thing, GpioPinDigitalOutput> pins = new HashMap<>();

	@Override
	public boolean isSupportedThing(Thing thing) {
		return thing instanceof LampOrBulb;
	}

	@Override
	public Boolean executeAction(JsonObject recivedPayload, Thing thing) {
		String action = recivedPayload.getString("action");
		if (thing instanceof LampOrBulb) {
			if (action.equals(ON)) {
				fireTriggers(TriggerType.BEFORE, action, thing, null);

				GpioController gpio = Pi4JHelper.getGpioController();
				final GpioPinDigitalOutput pin = getOutputPinFromThing(gpio, thing);
				gpio.high(pin);

				fireTriggers(TriggerType.AFTER, action, thing, null);

				return true;
			} else if (action.equals(OFF)) {
				fireTriggers(TriggerType.BEFORE, action, thing, null);

				GpioController gpio = Pi4JHelper.getGpioController();
				final GpioPinDigitalOutput pin = getOutputPinFromThing(gpio, thing);
				gpio.low(pin);

				fireTriggers(TriggerType.AFTER, action, thing, null);
				return true;
			} else if (action.equals(TOGGLE)) {
				fireTriggers(TriggerType.BEFORE, action, thing, null);

				GpioController gpio = Pi4JHelper.getGpioController();
				final GpioPinDigitalOutput pin = getOutputPinFromThing(gpio, thing);
				if (pin.isHigh())
					gpio.low(pin);
				else
					gpio.high(pin);

				fireTriggers(TriggerType.AFTER, action, thing, null);
				return true;
			}
		}
		return false;
	}

	protected GpioPinDigitalOutput getOutputPinFromThing(GpioController gpio, Thing thing) {
		if (!pins.containsKey(thing)) {
			int pinNumber = ((LampOrBulb) thing).getPin();
			GpioPinDigitalOutput result = Pi4JHelper.getDigitalOutputPin(gpio, pinNumber, "LAMP");
			pins.put(thing, result);
		}
		return pins.get(thing);
	}

	@Override
	public String toString() {
		return "LampOrBulbActuator [pins=" + pins + "]";
	}

}
