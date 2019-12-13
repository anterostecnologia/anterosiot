package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.support.colors.RGB;
import br.com.anteros.iot.things.RingStripLED12;
import br.com.anteros.iot.things.test.LEDDisplayType;

@JsonTypeName(DomainConstants.RING_STRIP_LED12)
public class RingStripLED12Node extends ThingNode {

	private int pin;
	private LEDDisplayType ledType;
	private int animateMiliseconds;
	private RGB color;
	protected int brightness = 64;	
	protected int numPixels = 60;


	public RingStripLED12Node() {
		super();
	}

	public RingStripLED12Node(String itemName, String description, int pin, LEDDisplayType ledType, int animateMiliseconds, RGB color,
			int brightness, int numPixels) {
		super(itemName, description);
		this.pin = pin;
		this.ledType = ledType;
		this.animateMiliseconds = animateMiliseconds;
		this.color = color;
		this.brightness = brightness;
		this.numPixels = numPixels;
	}
	
	public RingStripLED12Node(String itemName, String description, int pin, LEDDisplayType ledType, int animateMiliseconds, RGB color) {
		super(itemName, description);
		this.pin = pin;
		this.ledType = ledType;
		this.animateMiliseconds = animateMiliseconds;
		this.color = color;
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException {
		return mapper.writeValueAsString(node);
	}
	
	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new RingStripLED12(this);
	}

	public LEDDisplayType getLedType() {
		return ledType;
	}

	public void setLedType(LEDDisplayType ledType) {
		this.ledType = ledType;
	}

	public int getAnimateMiliseconds() {
		return animateMiliseconds;
	}

	public void setAnimateMiliseconds(int animateMiliseconds) {
		this.animateMiliseconds = animateMiliseconds;
	}

	public RGB getColor() {
		return color;
	}

	public void setColor(RGB color) {
		this.color = color;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public int getNumPixels() {
		return numPixels;
	}

	public void setNumPixels(int numPixels) {
		this.numPixels = numPixels;
	}
}
