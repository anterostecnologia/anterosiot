package br.com.anteros.iot.things;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.collectors.CollectResult;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.CameraMotionDetectorNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.processors.Processor;
import br.com.anteros.iot.triggers.Trigger;

public class CameraMotionDetector extends PlantItem implements Thing, Publishable {

	protected DeviceController deviceController;
	protected Set<Trigger> triggers = new HashSet<>();	

	protected String[] topics;
	protected String url;
	protected List<Processor<?>> processors = new ArrayList<>();
	
	public CameraMotionDetector() {
	}

	public CameraMotionDetector(CameraMotionDetectorNode node) {
		this.loadConfiguration(node);
	}
	
	@Override
	public String getThingID() {
		return itemId;
	}

	@Override
	public ThingStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Part> getParts() {
		return Collections.unmodifiableSet(new HashSet<>());
	}

	@Override
	public boolean hasParts() {
		return false;
	}

	@Override
	public Thing addPart(Part part) {
		return this;
	}

	@Override
	public Thing removePart(Part part) {
		return this;
	}

	@Override
	public Part getPartById(String part) {
		return null;
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		CameraMotionDetectorNode cameraNode = (CameraMotionDetectorNode) node;
		this.topics = cameraNode.getTopics();
		this.url = cameraNode.getUrl();
		this.itemId = cameraNode.getItemName();
		return this;
	}

	public DeviceController getDeviceController() {
		return deviceController;
	}

	public void setDeviceController(DeviceController deviceController) {
		this.deviceController = deviceController;
	}

	@Override
	public String[] getActions() {
		// TODO Auto-generated method stub
		return null;
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
	public Trigger[] getTriggers() {
		return triggers.toArray(new Trigger[] {});
	}

	@Override
	protected boolean acceptThisTypeOfPlantItem(Class<?> child) {
		return false;
	}

	@Override
	public String[] getTopicsToPublishValue(CollectResult collectedData) {
		return topics;
	}
	
	@Override
	public Thing addProcessor(Processor<?> processor) {
		processors.add(processor);
		return this;
	}

	@Override
	public Thing removeProcessor(Processor<?> processor) {
		processors.remove(processor);
		return this;
	}

	@Override
	public Processor<?>[] getProcessors() {
		return processors.toArray(new Processor[] {});
	}
}
