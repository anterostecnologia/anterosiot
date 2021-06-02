package br.com.anteros.iot.things;

import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.triggers.Trigger;

public class CameraALPR extends PlantItem implements Thing {
	
	protected DeviceController deviceController;
	protected boolean needsPropagation;
	protected Set<Trigger> triggers = new HashSet<>();
	protected Object userData;
	
	public CameraALPR() {
	}

	public String getThingID() {
		return itemId;
	}

	public String getStatus() {
		return null;
	}
	
	public void setStatus(java.lang.String status) {
		
	}

	public Set<Part> getParts() {
		return null;
	}

	public boolean hasParts() {
		return false;
	}

	public Thing addPart(Part part) {
		return null;
	}

	public Thing removePart(Part part) {
		return null;
	}

	public Part getPartById(String part) {
		return null;
	}

	@Override
	public Object getUserData() {
		return userData;
	}

	@Override
	public void setUserData(Object data){
		this.userData = data;
	}

	@Override
	public String getThingType() {
		return DomainConstants.CAMERA_ALPR;
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		return null;
	}

	@Override
	protected boolean acceptThisTypeOfPlantItem(Class<?> child) {
		return false;
	}

	public DeviceController getDeviceController() {
		return deviceController;
	}

	public void setDeviceController(DeviceController deviceController) {
		this.deviceController = deviceController;
	}
	
	@Override
	public Trigger[] getTriggers() {
		return triggers.toArray(new Trigger[] {});
	}


	@Override
	public Thing addTrigger(Trigger trigger) {
		triggers.add(trigger);
		return this;
	}

	@Override
	public Thing removeTrigger(Trigger trigger) {
		triggers.remove(trigger);
		return this;
	}

	@Override
	public String[] getActions() {
		return null;
	}

	@Override
	public String toString() {
		return "CameraALPR [itemId=" + itemId + ", description=" + description + "]";
	}	
	
	@Override
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}
}
