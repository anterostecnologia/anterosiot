package br.com.anteros.iot.things;

import java.util.Set;

import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.plant.Place;

public class EletronicLock implements Thing {

	protected Place place;

	public EletronicLock() {
		// TODO Auto-generated constructor stub
	}

	public Place getPlace() {
		return place;
	}

	public String getThingID() {
		// TODO Auto-generated method stub
		return null;
	}

	public ThingStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Part> getParts() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasParts() {
		// TODO Auto-generated method stub
		return false;
	}

	public Thing addPart(Part part) {
		// TODO Auto-generated method stub
		return null;
	}

	public Thing removePart(Part part) {
		// TODO Auto-generated method stub
		return null;
	}

	public Part getPartById(String part) {
		// TODO Auto-generated method stub
		return null;
	}

}
