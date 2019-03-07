package br.com.anteros.iot.domain.things.parts;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PartNode;
import br.com.anteros.iot.protocol.modbus.type.CollectType;
import br.com.anteros.iot.protocol.modbus.type.ModifyType;
import br.com.anteros.iot.things.parts.MemoryPlc;

@JsonTypeName(DomainConstants.MEMORY_PLC)
public class MemoryPlcNode extends PartNode {

	private int registerAddress;
	private CollectType collectType;
	private Object value;
	private ModifyType modifyType;
	private int multiple;


	public MemoryPlcNode(String itemName, String description, int registerAddress, CollectType type,
			ModifyType modifyType, int multiple) {
		super(itemName, description);
		this.registerAddress = registerAddress;
		this.collectType = type;
		this.modifyType = modifyType;
		this.multiple = multiple;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new MemoryPlc(this);
	}

	public MemoryPlcNode() {
		super();
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

	public int getRegisterAddress() {
		return registerAddress;
	}

	public void setRegisterAddress(int registerAddress) {
		this.registerAddress = registerAddress;
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
	

}
