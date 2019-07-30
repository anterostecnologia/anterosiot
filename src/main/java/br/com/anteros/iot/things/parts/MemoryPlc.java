package br.com.anteros.iot.things.parts;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.parts.MemoryPlcNode;
import br.com.anteros.iot.parts.exception.IllegalPartException;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.protocol.modbus.type.CollectType;
import br.com.anteros.iot.protocol.modbus.type.ModifyType;
import br.com.anteros.iot.things.Publishable;
import br.com.anteros.iot.triggers.Trigger;

public class MemoryPlc extends PlantItem implements Part, Publishable {

	protected int registerAddress;
	protected CollectType collectType;
	protected Object value;
	protected ModifyType modifyType;
	protected DeviceController deviceController;
	protected Set<Trigger> triggers = new HashSet<>();
	protected int multiple;

	public MemoryPlc(MemoryPlcNode node) {
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		this.registerAddress = node.getRegisterAddress();
		this.collectType = node.getCollectType();
		this.value = node.getValue();
		this.modifyType = node.getModifyType();
		this.multiple = node.getMultiple();
	}

	

	@Override
	public String getThingID() {
		return this.itemId;
	}

	@Override
	public ThingStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Part> getParts() {
		return Collections.unmodifiableSet(new HashSet<Part>());
	}

	@Override
	public boolean hasParts() {
		return false;
	}

	@Override
	public Thing addPart(Part part) {
		throw new IllegalPartException("Esta parte não permite a composição com mais partes.");
	}

	@Override
	public Thing removePart(Part part) {
		throw new IllegalPartException("Esta parte não permite a composição com mais partes.");
	}

	@Override
	public Part getPartById(String part) {
		return null;
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDeviceController(DeviceController deviceController) {
		this.deviceController = deviceController;
	}

	@Override
	public DeviceController getDeviceController() {
		return this.deviceController;
	}

	@Override
	public Thing getOwner() {
		return (Thing) itemOwner;
	}

	@Override
	protected boolean acceptThisTypeOfPlantItem(Class<?> child) {
		return false;
	}

	@Override
	public String[] getActions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Thing addTrigger(Trigger trigger) {
		this.triggers.add(trigger);
		return this;
	}

	@Override
	public Thing removeTrigger(Trigger trigger) {
		this.triggers.remove(trigger);
		return this;
	}

	@Override
	public Trigger[] getTriggers() {
		return this.triggers.toArray(new Trigger[] {});
	}

	public int getRegisterAddress() {
		return registerAddress;
	}

	public void setRegisterAddress(int registerAddress) {
		this.registerAddress = registerAddress;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public ModifyType getModifyType() {
		return modifyType;
	}

	public void setModifyType(ModifyType modifyType) {
		this.modifyType = modifyType;
	}

	public void setTriggers(Set<Trigger> triggers) {
		this.triggers = triggers;
	}

	@Override
	public String[] getTopicsToPublishValue(CollectResult collectedData) {
		return new String[] { getPath() };
	}

	public CollectType getCollectType() {
		return collectType;
	}

	public void setCollectType(CollectType collectType) {
		this.collectType = collectType;
	}
	
	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	@Override
	public String toString() {
		return "MemoryPlc [itemOwner=" + itemOwner + ", itemId=" + itemId + ", description=" + description + "]";
	}
	
	
}
