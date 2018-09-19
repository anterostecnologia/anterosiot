package br.com.anteros.iot.things.devices;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.Device;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.plant.Place;

public class RaspberryPIZero implements Device  {
	
	protected Place place;
	protected String thingId;
	protected IpAddress ipAddress;
		
	protected RaspberryPIZero(String id, IpAddress ipAddress) {
		this.thingId = id;
		this.ipAddress = ipAddress;
	}

	public Place getPlace() {
		return place;
	}

	public String getThingID() {
		return thingId;
	}

	public ThingStatus getStatus() {
		return null;
	}

	public Set<Part> getParts() {
		return Collections.unmodifiableSet(new HashSet<Part>());
	}

	public boolean hasParts() {
		return false;
	}

	public Thing addPart(Part part) {
		return this;
	}

	public Thing removePart(Part part) {
		return this;
	}

	public IpAddress getIpAddress() {
		return ipAddress;
	}

	public Part getPartById(String part) {
		// TODO Auto-generated method stub
		return null;
	}

}
