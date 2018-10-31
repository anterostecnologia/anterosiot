package br.com.anteros.iot.actuators;

import java.util.Properties;

import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.iot.Actuator;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.protocol.modbus.ModbusProtocolDevice;
import br.com.anteros.iot.protocol.modbus.ModbusProtocolDeviceService;
import br.com.anteros.iot.protocol.modbus.ModbusProtocolException;
import br.com.anteros.iot.protocol.modbus.type.CollectType;
import br.com.anteros.iot.protocol.modbus.type.ModifyType;
import br.com.anteros.iot.things.Plc;
import br.com.anteros.iot.things.parts.MemoryPlc;
import br.com.anteros.iot.triggers.TriggerType;

public class MemoryPlcActuator implements Actuator<Boolean> {

	private ModbusProtocolDeviceService protocolDevice;
	private Properties modbusProperties;

	@Override
	public boolean isSupportedThing(Thing thing) {
		return thing instanceof MemoryPlc;
	}

	@Override
	public Boolean executeAction(String action, Thing thing) {
		MemoryPlc memory = (MemoryPlc) thing;

		if (memory.getModifyType().equals(ModifyType.READ)) {
			System.out.println("A memória do PLC selecioando não pode alterar o valor, verifique.");
			return false;
		}

		if (this.protocolDevice != null) {
			try {
				this.protocolDevice.disconnect();
			} catch (ModbusProtocolException e) {
				System.out.println("Failed to disconnect : " + e.getMessage());
				return false;
			}
		} else {
			this.protocolDevice = new ModbusProtocolDevice();
		}

		Plc plc = (Plc) memory.getOwner();
		this.modbusProperties = getModbusProperties(plc);

		try {
			configureDevice();

			if (memory.getCollectType().equals(CollectType.COIL)) {
				if (action.equals("on")) {
					fireTriggers(TriggerType.BEFORE, action, thing, null);

					this.protocolDevice.writeSingleCoil(plc.getSlaveAddress(), memory.getRegisterAddress(), true);

					fireTriggers(TriggerType.AFTER, action, thing, null);
					return true;
				} else if (action.equals("off")) {
					fireTriggers(TriggerType.BEFORE, action, thing, null);

					this.protocolDevice.writeSingleCoil(plc.getSlaveAddress(), memory.getRegisterAddress(), false);

					fireTriggers(TriggerType.AFTER, action, thing, null);
					return true;
				}
			} else if (memory.getCollectType().equals(CollectType.INPUTREGISTER)) {

				if (StringUtils.isInteger(action)) {
					fireTriggers(TriggerType.BEFORE, action, thing, null);

					this.protocolDevice.writeSingleRegister(plc.getSlaveAddress(), memory.getRegisterAddress(),
							Integer.parseInt(action));

					fireTriggers(TriggerType.AFTER, action, thing, null);
					return true;
				}
			}
		} catch (ModbusProtocolException e) {
			System.out.println(e.getMessage());
		}

		return false;
	}

	private void configureDevice() throws ModbusProtocolException {
		if (this.protocolDevice != null) {
			this.protocolDevice.disconnect();

			this.protocolDevice.configureConnection(this.modbusProperties);
		}
	}

	private Properties getModbusProperties(Plc plc) {

		Properties prop = new Properties();

		prop.setProperty("connectionType", plc.getModbusProtocol());
		prop.setProperty("slaveAddr", String.valueOf(plc.getSlaveAddress()));
		prop.setProperty("ethport", String.valueOf(plc.getPort()));
		prop.setProperty("ipAddress", plc.getIp());
		prop.setProperty("respTimeout", String.valueOf(plc.getTimeOut()));
		prop.setProperty("mode", "0");
		prop.setProperty("transmissionMode", "RTU");

		return prop;
	}
}
