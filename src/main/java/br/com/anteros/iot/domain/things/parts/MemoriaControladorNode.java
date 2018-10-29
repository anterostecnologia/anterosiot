package br.com.anteros.iot.domain.things.parts;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.PartNode;
import br.com.anteros.iot.protocol.modbus.type.CollectType;
import br.com.anteros.iot.protocol.modbus.type.ModifyType;
import br.com.anteros.iot.things.parts.MemoriaControlador;

public class MemoriaControladorNode extends PartNode {

	private int registerAddress;
	private CollectType type;
	private Object value;
	private ModifyType modifyType;

	public MemoriaControladorNode(String itemName, String description, int registerAddress, CollectType type,
			ModifyType modifyType) {
		super(itemName, description);
		this.registerAddress = registerAddress;
		this.type = type;
		this.modifyType = modifyType;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new MemoriaControlador(this);
	}

	public MemoriaControladorNode() {
		super();
	}

	public CollectType getType() {
		return type;
	}

	public void setType(CollectType type) {
		this.type = type;
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

}
