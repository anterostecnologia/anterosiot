package br.com.anteros.iot.actuators.collectors;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.diozero.util.SleepUtil;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.core.utils.ObjectUtils;
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

	private static final Logger logger = LogManager.getLogger(PlcColletor.class);
	protected Boolean running = false;
	protected Thread thread;

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

		Plc plc = (Plc) thing;

		logger.info("Iniciando coletor do PLC " + plc.getItemId() + " - " + plc.getDescription());

		while (running) {

			ModbusProtocolDeviceService protocolDevice = new ModbusProtocolDevice();

			for (Part part : plc.getMemories()) {

				MemoryPlc memory = (MemoryPlc) part;

				Object valorResult = doModbusLoop(memory, protocolDevice, getModbusProperties(plc));

				if (!ObjectUtils.isEmpty(valorResult)) {

					if (memory.getValue() == null || valorResult != memory.getValue()) {

						Object oldValue = memory.getValue();
						memory.setValue(valorResult);

						if (listener != null) {
							listener.onCollect(ModbusResult.of(oldValue, memory.getValue()), memory);
						}
					}
				}
			}
			SleepUtil.sleepMillis(plc.getInterval());
		}
	}

	private Object doModbusLoop(MemoryPlc memory, ModbusProtocolDeviceService protocolDevice,
			Properties modbusProperties) {
		try {
			protocolDevice.configureConnection(modbusProperties);

			if (memory.getCollectType().equals(CollectType.COIL)) {
				boolean[] readCoils = protocolDevice.readCoils(((Plc) memory.getOwner()).getSlaveAddress(),
						memory.getRegisterAddress(), 1);
				protocolDevice.disconnect();
				return readCoils[0];
			} else {
				int[] analogInputs = protocolDevice.readHoldingRegisters(((Plc) memory.getOwner()).getSlaveAddress(),
						memory.getRegisterAddress(), 1);
				protocolDevice.disconnect();
				return analogInputs[0];
			}

		} catch (Exception e) {
			if (memory.getProcessors() != null && memory.getProcessors().length > 0)
				logger.error(e.getMessage());
			return null;
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
