package br.com.anteros.iot.collectors;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.diozero.util.SleepUtil;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.protocol.modbus.ModbusProtocolDevice;
import br.com.anteros.iot.protocol.modbus.ModbusProtocolDeviceService;
import br.com.anteros.iot.protocol.modbus.ModbusProtocolException;
import br.com.anteros.iot.protocol.modbus.type.CollectType;
import br.com.anteros.iot.things.Plc;
import br.com.anteros.iot.things.parts.MemoryPlc;

public class PlcColletor extends Collector implements Runnable {

	protected Boolean running = false;
	protected Thread thread;

	Map<String, ResultValue> valueCache = new HashMap<>();

	private ModbusProtocolDeviceService protocolDevice;
	private Properties modbusProperties;

	public PlcColletor(CollectorListener listener, Thing thing) {
		super(listener, thing);
	}

	public PlcColletor() {
	}

	@Override
	public boolean isSupportedThing(Thing thing) {
		return thing instanceof Plc;
	}

	@Override
	public void startCollect() {
		Assert.notNull(listener);
		if (thing instanceof Plc) {
			this.running = true;
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void stopCollect() {
		this.running = false;
	}

	@Override
	public void run() {
		Plc controlador = (Plc) thing;

		System.out
				.println("Iniciando coletor do PLC " + controlador.getItemId() + " - " + controlador.getDescription());

		this.protocolDevice = new ModbusProtocolDevice();

		if (this.protocolDevice != null) {
			try {
				this.protocolDevice.disconnect();
			} catch (ModbusProtocolException e) {
				System.out.println("Failed to disconnect : " + e.getMessage());
			}
		}

		this.modbusProperties = getModbusProperties(controlador);

		try {

			configureDevice();

		} catch (ModbusProtocolException e) {
			System.out.println("ModbusProtocolException : " + e.getMessage());
		}

		while (running) {

			for (Part part : controlador.getMemorias()) {

				MemoryPlc memoria = (MemoryPlc) part;

				Object valorResult = doModbusLoop(memoria);

				ResultValue resultValue = valueCache.get(memoria.getItemId());
				if (resultValue == null) {
					resultValue = new ResultValue();
				}

				if (resultValue.newValue == null || valorResult != resultValue.newValue) {
					resultValue.oldValue = resultValue.newValue;
					resultValue.newValue = valorResult;

					if (listener != null) {
						valueCache.put(memoria.getItemId(), resultValue);
						listener.onCollect(ModbusResult.of(resultValue.oldValue, resultValue.newValue), memoria);
					}
				}
			}
		}
		SleepUtil.sleepMillis(500);
	}

	private Object doModbusLoop(MemoryPlc memoria) {
		try {

			if (memoria.getType() == CollectType.COIL) {
				boolean[] readCoils = this.protocolDevice.readCoils(((Plc) memoria.getOwner()).getSlaveAddress(),
						memoria.getRegisterAddress(), 1);
				return readCoils[0];
			} else {
				int[] analogInputs = this.protocolDevice.readInputRegisters(
						((Plc) memoria.getOwner()).getSlaveAddress(), memoria.getRegisterAddress(), 1);
				return analogInputs[0];
			}
		} catch (ModbusProtocolException e) {
			return null;
		}
	}

	private void configureDevice() throws ModbusProtocolException {
		if (this.protocolDevice != null) {
			this.protocolDevice.disconnect();

			this.protocolDevice.configureConnection(this.modbusProperties);
		}
	}
		
	private Properties getModbusProperties(Plc controlador) {

		Properties prop = new Properties();

		prop.setProperty("connectionType", controlador.getModbusProtocol());
		prop.setProperty("slaveAddr", String.valueOf(controlador.getSlaveAddress()));
		prop.setProperty("ethport", String.valueOf(controlador.getPort()));
		prop.setProperty("ipAddress", controlador.getIp());
		prop.setProperty("respTimeout", String.valueOf(controlador.getTimeOut()));
		prop.setProperty("mode", "0");
		prop.setProperty("transmissionMode", "RTU");

		return prop;
	}

	class ResultValue {
		protected Object oldValue;
		protected Object newValue;
	}
}