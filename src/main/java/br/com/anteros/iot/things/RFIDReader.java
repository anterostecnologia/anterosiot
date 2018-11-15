package br.com.anteros.iot.things;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.anteros.core.utils.ArrayUtils;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.RFIDModel;
import br.com.anteros.iot.domain.things.RFIDReaderNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.processors.Processor;
import br.com.anteros.iot.triggers.Trigger;

public class RFIDReader extends PlantItem implements Thing, Publishable {

	protected DeviceController deviceController;

	protected String[] topics;
	protected RFIDModel model;
	protected Set<Trigger> triggers = new HashSet<>();
	protected List<Processor<?>> processors = new ArrayList<>();

	public RFIDReader(PlantItemNode node) {
		this.loadConfiguration(node);
	}

	public String getThingID() {
		return itemId;
	}

	public ThingStatus getStatus() {
		return null;
	}

	public Set<Part> getParts() {
		return Collections.unmodifiableSet(new HashSet<>());
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

	public Part getPartById(String part) {
		return null;
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		this.model = ((RFIDReaderNode) node).getModel();
		this.topics = ((RFIDReaderNode) node).getTopics();
		return this;
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

	public RFIDModel getModel() {
		return model;
	}

	public void setModel(RFIDModel model) {
		this.model = model;
	}

	@Override
	public String[] getTopicsToPublishValue(CollectResult collectedData) {
		System.out.println(ArrayUtils.toString(new String[] { this.getPath() }));
		if (topics == null || topics.length == 0) {
			return new String[] { this.getPath() };
		}
		return topics;
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
		// TODO Auto-generated method stub
		return null;
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
