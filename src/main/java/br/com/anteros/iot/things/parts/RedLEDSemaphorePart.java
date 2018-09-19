package br.com.anteros.iot.things.parts;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.parts.exception.IllegalPartException;
import br.com.anteros.iot.plant.Place;
import br.com.anteros.iot.things.Semaphore;

public class RedLEDSemaphorePart implements Part, LedSemaphore {

	private Semaphore owner;
	private int pin;
	private String thingId;

	private RedLEDSemaphorePart(String id, Semaphore owner, int pin) {
		this.owner = owner;
		this.pin = pin;
		this.thingId = id;
	}

	public Place getPlace() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getThingID() {
		return thingId;
	}

	public ThingStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Part> getParts() {
		return Collections.unmodifiableSet(new HashSet<Part>());
	}

	public boolean hasParts() {
		return false;
	}

	public Thing addPart(Part part) {
		throw new IllegalPartException("Esta parte não permite a composição com mais partes.");
	}

	public Thing removePart(Part part) {
		throw new IllegalPartException("Esta parte não permite a composição com mais partes.");
	}

	public Thing getOwner() {
		return owner;
	}

	public static RedLEDSemaphorePart of(String id, Semaphore semaphore, int pin) {
		return new RedLEDSemaphorePart(id,semaphore, pin) ;
	}

	public int getPin() {
		return pin;
	}

	public Part getPartById(String part) {
		return null;
	}

}
