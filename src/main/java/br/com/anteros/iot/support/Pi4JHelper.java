package br.com.anteros.iot.support;

import java.lang.reflect.Field;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import br.com.anteros.core.utils.StringUtils;

public class Pi4JHelper {

	public static GpioController getGpioController() {
		return GpioFactory.getInstance();
	}

	public static GpioPinDigitalOutput getDigitalOutputPin(GpioController gpio, int pinNumber, String prefix) {
		Pin pin = (Pin) getVariableValue(RaspiPin.class, "GPIO_" + StringUtils.padZero(pinNumber + "", 2));
		return gpio.provisionDigitalOutputPin(pin, prefix + "_" + pinNumber);
	}

	public static Object getVariableValue(Class<?> c, String fieldName) {

		Object result = null;
		try {
			Field f = c.getDeclaredField(fieldName);
			f.setAccessible(true);
			if (f.isAccessible()) {
				result = f.get(null);
			} else {
				return null;
			}

		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

}
