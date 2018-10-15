package br.com.anteros.iot.support;

import java.lang.reflect.Field;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import br.com.anteros.core.utils.StringUtils;

public class Pi4JHelper {
	
	public static final int GPIO_00 = 0;
    public static final int GPIO_01 =  1;
    public static final int GPIO_02 = 2;
    public static final int GPIO_03 = 3;
    public static final int GPIO_04 = 4;
    public static final int GPIO_05 = 5;
    public static final int GPIO_06 = 6;
    public static final int GPIO_07 = 7;
    public static final int GPIO_08 = 8; 
    public static final int GPIO_09 = 9; 
    public static final int GPIO_10 = 10;
    public static final int GPIO_11 = 11;
    public static final int GPIO_12 = 12;
    public static final int GPIO_13 = 13;
    public static final int GPIO_14 = 14;
    public static final int GPIO_15 = 15;
    public static final int GPIO_16 = 16;

    // the following GPIO Strings are only available on the Raspberry Pi Model A, B (revision 2.0)
    public static final int GPIO_17 = 17;
    public static final int GPIO_18 = 18;
    public static final int GPIO_19 = 19;
    public static final int GPIO_20 = 20;

    // the following GPIO Strings are only available on the Raspberry Pi Model A+, B+, Model 2B, Model 3B, Zero
    public static final int GPIO_21 = 21;
    public static final int GPIO_22 = 22;
    public static final int GPIO_23 = 23;
    public static final int GPIO_24 = 24;
    public static final int GPIO_25 = 25;
    public static final int GPIO_26 = 26;
    public static final int GPIO_27 = 27;
    public static final int GPIO_28 = 28;
    public static final int GPIO_29 = 29;
    public static final int GPIO_30 = 30;
    public static final int GPIO_31 = 31;

	public static GpioController getGpioController() {
		return GpioFactory.getInstance();
	}

	public static GpioPinDigitalOutput getDigitalOutputPin(GpioController gpio, int pinNumber, String prefix) {
		Pin pin = (Pin) getVariableValue(RaspiPin.class, "GPIO_" + StringUtils.padZero(pinNumber + "", 2));
		return gpio.provisionDigitalOutputPin(pin, prefix + "_" + pinNumber);
	}
	
	public static GpioPinDigitalInput getDigitalInputPin(GpioController gpio, int pinNumber) {
		Pin pin = (Pin) getVariableValue(RaspiPin.class, "GPIO_" + StringUtils.padZero(pinNumber + "", 2));
		return gpio.provisionDigitalInputPin(pin);
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
