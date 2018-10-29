package br.com.anteros.iot.things;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.ThingStatus;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.domain.things.ControladorNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.things.exception.ThingException;
import br.com.anteros.iot.things.parts.MemoriaControlador;
import br.com.anteros.iot.triggers.Trigger;

public class Controlador extends PlantItem implements Thing {

	protected String modbusProtocol;
	protected String ip;
	protected long port;
	protected long interval;
	protected long timeOut;
	protected int slaveAddress;
	
	public Controlador() {
		super();
	}
	
	protected DeviceController deviceController;

	protected Set<Trigger> triggers = new HashSet<>();

	protected Set<Part> memorias = new LinkedHashSet<Part>();

	public Controlador(PlantItemNode node) {
		loadConfiguration(node);
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
		return memorias;
	}

	@Override
	public boolean hasParts() {
		return !memorias.isEmpty();
	}

	@Override
	public Thing addPart(Part part) {
		if (!(part instanceof MemoriaControlador)) {
			throw new ThingException("Tipo de parte inválida para uso com Controlador.");
		}
		if (part instanceof PlantItem) {
			((PlantItem) part).setItemOwner(this);
		}
		memorias.add(part);
		return this;
	}

	@Override
	public Thing removePart(Part part) {
		memorias.remove(part);
		return this;
	}

	@Override
	public Part getPartById(String part) {
		for (Part p : memorias) {
			if (p.getThingID().equals(part)) {
				return p;
			}
		}
		return null;
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		this.itemId = node.getItemName();
		this.description = node.getDescription();
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
		return child.equals(MemoriaControlador.class);
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

	public Set<Part> getMemorias() {
		return memorias;
	}

	public void setMemorias(Set<Part> memorias) {
		this.memorias = memorias;
	}

	public void setTriggers(Set<Trigger> triggers) {
		this.triggers = triggers;
	}

}
