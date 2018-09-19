package br.com.anteros.iot;

import java.util.Set;

import br.com.anteros.iot.plant.Place;

public interface Thing {

	public Place getPlace();
	
	public String getThingID(); // UUID
	
	public ThingStatus getStatus();
	
	public Set<Part> getParts();
	
	public boolean hasParts();
	
	public Thing addPart(Part part);
	
	public Thing removePart(Part part);

	public Part getPartById(String part);
	
}
