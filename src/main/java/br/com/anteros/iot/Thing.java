package br.com.anteros.iot;

import java.util.Set;

import br.com.anteros.iot.domain.PlantItemNode;

public interface Thing {

	public String getThingID(); // UUID

	public ThingStatus getStatus();

	public Set<Part> getParts();

	public boolean hasParts();

	public Thing addPart(Part part);

	public Thing removePart(Part part);

	public Part getPartById(String part);
	
	public Thing loadConfiguration(PlantItemNode node);
	
	public void setDeviceController(DeviceController controller);
	
	public DeviceController getDeviceController();

	
}
