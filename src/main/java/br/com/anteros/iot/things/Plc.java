package br.com.anteros.iot.things;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.domain.things.PlcNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.things.exception.ThingException;
import br.com.anteros.iot.things.parts.MemoryPlc;
import br.com.anteros.iot.triggers.Trigger;

public class Plc extends PlantItem implements Thing {

	protected String modbusProtocol;
	protected String ip;
	protected long port;
	protected long interval;
	protected long timeOut;
	protected int slaveAddress;
	protected DeviceController deviceController;
	protected boolean needsPropagation;
	protected Set<Trigger> triggers = new HashSet<>();
	protected Set<Part> memories = new LinkedHashSet<Part>();
	protected Object userData;
	protected String status;
	protected String lastValue;
	
	public Plc() {
		super();
	}
	
	public Plc(PlantItemNode node) {
		loadConfiguration(node);
	}

	@Override
	public String getThingID() {
		return this.itemId;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	@Override
	public String getLastValue() {
		return lastValue;
	}

	@Override
	public void setLastValue(String value) {
		this.lastValue = value;
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
		return DomainConstants.PLC;
	}

	@Override
	public Set<Part> getParts() {
		return memories;
	}

	@Override
	public boolean hasParts() {
		return !memories.isEmpty();
	}

	@Override
	public Thing addPart(Part part) {
		if (!(part instanceof MemoryPlc)) {
			throw new ThingException("Tipo de parte inválida para uso com PLC.");
		}
		if (part instanceof PlantItem) {
			((PlantItem) part).setItemOwner(this);
		}
		memories.add(part);
		return this;
	}

	@Override
	public Thing removePart(Part part) {
		memories.remove(part);
		return this;
	}

	@Override
	public Part getPartById(String part) {
		for (Part p : memories) {
			if (p.getThingID().equals(part)) {
				return p;
			}
		}
		return null;
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		PlcNode controladorNode = (PlcNode) node;
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		this.modbusProtocol = controladorNode.getModbusProtocol();
		this.ip = controladorNode.getIp();
		this.port = controladorNode.getPort();
		this.interval = controladorNode.getInterval();
		this.timeOut = controladorNode.getTimeOut();
		this.slaveAddress = controladorNode.getSlaveAddress();

		for (PlantItemNode child : node.getItems()) {
			if (child instanceof ThingNode) {
				Thing part = child.getInstanceOfThing();
				((PlantItem) part).setItemOwner(this);
				if (part instanceof Part) {
					this.addPart((Part) part);
				}
			}
		}
		return this;
	}

	@Override
	public void setDeviceController(DeviceController deviceController) {
		this.deviceController = deviceController;
	}

	@Override
	public DeviceController getDeviceController() {
		return deviceController;
	}

	@Override
	protected boolean acceptThisTypeOfPlantItem(Class<?> child) {
		return child.equals(MemoryPlc.class);
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

	public String getModbusProtocol() {
		return modbusProtocol;
	}

	public void setModbusProtocol(String modbusProtocol) {
		this.modbusProtocol = modbusProtocol;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getPort() {
		return port;
	}

	public void setPort(long port) {
		this.port = port;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public int getSlaveAddress() {
		return slaveAddress;
	}

	public void setSlaveAddress(int slaveAddress) {
		this.slaveAddress = slaveAddress;
	}

	public void setTriggers(Set<Trigger> triggers) {
		this.triggers = triggers;
	}

	public Set<Part> getMemories() {
		return memories;
	}

	public void setMemories(Set<Part> memories) {
		this.memories = memories;
	}
	
	@Override
	public String toString() {
		return "Plc [itemOwner=" + itemOwner + ", itemId=" + itemId + ", description=" + description + "]";
	}
	
	@Override
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}
}
