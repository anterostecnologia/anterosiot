package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PartNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.Plc;

@JsonTypeName(DomainConstants.PLC)
public class PlcNode extends ThingNode {

	private String modbusProtocol;
	private String ip;
	private long port;
	private long interval;
	private long timeOut;
	private int slaveAddress;

	public PlcNode(String itemName, String description, String modbusProtocol, String ip, long port,
			long interval, long timeOut, int slaveAddress) {
		super(itemName, description);
		this.modbusProtocol = modbusProtocol;
		this.ip = ip;
		this.port = port;
		this.interval = interval;
		this.timeOut = timeOut;
		this.slaveAddress = slaveAddress;
	}

	public PlcNode() {
		super();
	}

	@Override
	public Thing getInstanceOfThing() {
		return new Plc(this);
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return child.equals(PartNode.class) || ReflectionUtils.isExtendsClass(PartNode.class, child);
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
}
