package br.com.anteros.iot.things;

import java.util.LinkedHashSet;
import java.util.Set;

import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.plant.Place;
import br.com.anteros.iot.things.exception.ThingException;
import br.com.anteros.iot.things.parts.GreenLEDSemaphorePart;
import br.com.anteros.iot.things.parts.LedSemaphore;
import br.com.anteros.iot.things.parts.RedLEDSemaphorePart;
import br.com.anteros.iot.things.parts.YellowLEDSemaphorePart;

public class Semaphore implements Thing {
	
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

	protected Place place;
	protected Set<Part> leds = new LinkedHashSet<Part>();
	protected String thingId;

	public Semaphore(String id) {
		this.thingId = id;
	}

	public Place getPlace() {
		return place;
	}

	public String getThingID() {
		return thingId;
	}

	public ThingStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Part> getParts() {
		return leds;
	}

	public boolean hasParts() {
		return !leds.isEmpty();
	}

	public Thing addPart(Part part) {
		if (!(part instanceof LedSemaphore)) {
			throw new ThingException("Tipo de parte inválida para uso com Semaphore.");
		}
		leds.add(part);
		return this;
	}

	public Thing removePart(Part part) {
		leds.remove(part);
		return this;
	}

	public Part getPartById(String part) {
		for (Part p : leds) {
			if (p.getThingID().equals(part)) {
				return p;
			}
		}
		return null;
	}

}
