package br.com.anteros.iot.actuators;

import java.util.Properties;

import javax.json.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
	private static final Logger logger = LogManager.getLogger(MemoryPlcActuator.class);
	private ModbusProtocolDeviceService protocolDevice;
	private Properties modbusProperties;

	@Override
	public boolean isSupportedThing(Thing thing) {
		return thing instanceof MemoryPlc;
	}

	@Override
	public Boolean executeAction(JsonObject recivedPayload, Thing thing) {
		String action = recivedPayload.get("action").toString();
		MemoryPlc memory = (MemoryPlc) thing;

		if (memory.getModifyType().equals(ModifyType.READ)) {
			logger.error("A memória do PLC selecioando não pode alterar o valor, verifique.");
			return false;
		}

		if (this.protocolDevice != null) {
			try {
				this.protocolDevice.disconnect();
			} catch (ModbusProtocolException e) {
				logger.error("Failed to disconnect : " + e.getMessage());
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
			logger.error(e.getMessage());
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
