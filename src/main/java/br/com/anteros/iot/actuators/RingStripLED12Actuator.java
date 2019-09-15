package br.com.anteros.iot.actuators;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

import com.diozero.util.SleepUtil;
import com.diozero.ws281xj.LedDriverInterface;
import com.diozero.ws281xj.PixelAnimations;
import com.diozero.ws281xj.PixelColour;
import com.diozero.ws281xj.rpiws281x.WS281x;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import br.com.anteros.iot.Actuator;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.support.colors.RGB;
import br.com.anteros.iot.things.RingStripLED12;
import br.com.anteros.iot.things.test.LEDDisplayType;
import br.com.anteros.iot.triggers.TriggerType;

public class RingStripLED12Actuator implements Actuator<Boolean> {

	public static String ON = "on";
	public static String OFF = "off";
	protected LedDriverInterface ledDriver;
	protected Map<Thing, GpioPinDigitalOutput> pins = new HashMap<>();
	protected Running running = Running.of(false);
	protected Thread thread;

	@Override
	public boolean isSupportedThing(Thing thing) {
		return thing instanceof RingStripLED12;
	}

	@Override
	public Boolean executeAction(JsonObject recivedPayload, Thing thing) {
		String action = recivedPayload.getString("action");
		if (action.equals(ON)) {
			if (thread != null)
				thread.interrupt();
			thread = new Thread(new ExecuteActionRunnable((RingStripLED12) thing));
			thread.setName("Led ring strip");
			thread.start();
		} else if (action.equals(OFF)) {
			running = Running.of(false);
			if (thread != null)
				thread.interrupt();
			LedDriverInterface ledDriver = getLedInterface((RingStripLED12) thing);
			ledDriver.allOff();
		}
		return true;
	}

	protected void simpleColor(RGB rgb, int animateMiliseconds) {
		for (int pixel = 0; pixel < ledDriver.getNumPixels(); pixel++) {
			ledDriver.setRedComponent(pixel, rgb.getRed());
			ledDriver.setGreenComponent(pixel, rgb.getGreen());
			ledDriver.setBlueComponent(pixel, rgb.getBlue());
			if (animateMiliseconds > 0)
				PixelAnimations.delay(animateMiliseconds);
		}
	}
	
	protected void simpleColorCountdown(LedDriverInterface ledDriver, RGB rgb, int wait , int timeMiliseconds) {
		int rgbInt = PixelColour.createColourRGB(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
		for (int i=0; i<ledDriver.getNumPixels(); i++) {
			ledDriver.setPixelColour(i, rgbInt);
			ledDriver.render();
			if (!running.isRunning()) {
				return;
			}
			SleepUtil.sleepMillis(wait);
		}
		
		for (int i=0; i<ledDriver.getNumPixels(); i++) {
			ledDriver.setPixelColour(i, 0);
			ledDriver.render();
			if (!running.isRunning()) {
				return;
			}
			SleepUtil.sleepMillis(timeMiliseconds/12);
		}
	}
	
	

	protected void rainbowColours(LedDriverInterface ledDriver) {
		int[] colours = PixelColour.RAINBOW;

		for (int i = 0; i < 250; i++) {
			for (int pixel = 0; pixel < ledDriver.getNumPixels(); pixel++) {
				ledDriver.setPixelColour(pixel, colours[(i + pixel) % colours.length]);
				if (!running.isRunning()) {
					return;
				}
			}	

			ledDriver.render();
			if (!running.isRunning()) {
				return;
			}
			PixelAnimations.delay(50);
		}
	}

	protected void hsb(LedDriverInterface ledDriver, int animatedMiliseconds) {
		float brightness = 0.5f;

		for (float hue = 0; hue < 1; hue += 0.05f) {
			for (float saturation = 0; saturation <= 1; saturation += 0.05f) {
				for (int pixel = 0; pixel < ledDriver.getNumPixels(); pixel++) {
					ledDriver.setPixelColourHSB(pixel, hue, saturation, brightness);
				}
				ledDriver.render();
				if (animatedMiliseconds > 0)
					PixelAnimations.delay(animatedMiliseconds);
				PixelAnimations.delay(20);
			}
		}
	}

	protected void hsl(LedDriverInterface ledDriver, int animatedMiliseconds) {
		float luminance = 0.5f;

		for (float hue = 0; hue < 360; hue += (360 / 20)) {
			for (float saturation = 0; saturation <= 1; saturation += 0.05f) {
				for (int pixel = 0; pixel < ledDriver.getNumPixels(); pixel++) {
					ledDriver.setPixelColourHSL(pixel, hue, saturation, luminance);
				}
				ledDriver.render();
				if (animatedMiliseconds > 0)
					PixelAnimations.delay(animatedMiliseconds);
			}
		}
	}

	protected synchronized LedDriverInterface getLedInterface(RingStripLED12 thing) {
		if (ledDriver == null) {
			ledDriver = new WS281x(thing.getPin(), thing.getBrightness(), thing.getNumPixels());
		}
		return ledDriver;
	}

	class ExecuteActionRunnable implements Runnable {
		protected RingStripLED12 ringLed;

		ExecuteActionRunnable(RingStripLED12 ringLed) {
			this.ringLed = ringLed;
		}

		@Override
		public void run() {
			running = Running.of(true);
			LedDriverInterface ledDriver = getLedInterface(ringLed);

			int rgb = PixelColour.createColourRGB(ringLed.getColor().getRed(), ringLed.getColor().getGreen(),
					ringLed.getColor().getBlue());

			fireTriggers(TriggerType.BEFORE, ON, ringLed, null);
			ledDriver.allOff();
			if (LEDDisplayType.COLOUR_WIPE_ANIMATED.equals(ringLed.getLedType())) {
				PixelAnimations.colourWipe(ledDriver, rgb, ringLed.getAnimateMiliseconds());
			} else if (LEDDisplayType.HSB_ANIMATED.equals(ringLed.getLedType())) {
				hsb(ledDriver, ringLed.getAnimateMiliseconds());
			} else if (LEDDisplayType.HSL_ANIMATED.equals(ringLed.getLedType())) {
				hsl(ledDriver, ringLed.getAnimateMiliseconds());
			} else if (LEDDisplayType.RAINBOW_ANIMATED.equals(ringLed.getLedType())) {
				PixelAnimations.rainbow(ledDriver, ringLed.getAnimateMiliseconds());
			} else if (LEDDisplayType.RAINBOW_COLOURS.equals(ringLed.getLedType())) {
				rainbowColours(ledDriver);
			} else if (LEDDisplayType.RAINBOW_CYCLE_ANIMATED.equals(ringLed.getLedType())) {
				PixelAnimations.rainbowCycle(ledDriver, 20);
			} else if (LEDDisplayType.SIMPLE_COLOR.equals(ringLed.getLedType())) {
				simpleColorCountdown(ledDriver, ringLed.getColor(), ringLed.getAnimateMiliseconds(), 8000);
			} else if (LEDDisplayType.THEATER_CHASE_ANIMATED.equals(ringLed.getLedType())) {
				PixelAnimations.theatreChase(ledDriver, rgb, ringLed.getAnimateMiliseconds());
			} else if (LEDDisplayType.THEATER_CHASE_RAINBOW_ANIMATED.equals(ringLed.getLedType())) {
				PixelAnimations.theatreChaseRainbow(ledDriver, ringLed.getAnimateMiliseconds());
			}

			fireTriggers(TriggerType.AFTER, ON, ringLed, null);
			running = Running.of(false);
		}

	}

}
