package br.com.anteros.iot.collectors;

import java.util.Properties;

import com.diozero.util.SleepUtil;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.protocol.modbus.ModbusProtocolDeviceService;
import br.com.anteros.iot.protocol.modbus.ModbusProtocolException;
import br.com.anteros.iot.protocol.modbus.type.CollectType;
import br.com.anteros.iot.things.Controlador;
import br.com.anteros.iot.things.parts.MemoriaControlador;

public class ControladorColletor extends Collector implements Runnable {

	protected Boolean running = false;
	protected Thread thread;
	protected Object oldValue;
	protected Object newValue;

	private ModbusProtocolDeviceService protocolDevice;
	private Properties modbusProperties;

	public ControladorColletor(CollectorListener listener, Thing thing) {
		super(listener, thing);
	}

	public ControladorColletor() {
	}

	@Override
	public boolean isSupportedThing(Thing thing) {
		return thing instanceof Controlador;
	}

	@Override
	public void startCollect() {
//		Assert.notNull(listener);
		if (thing instanceof Controlador) {
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
		System.out.println("Iniciando coletor da memória");

		Controlador controlador = (Controlador) thing;

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
				
				MemoriaControlador memoria = (MemoriaControlador) part;
				
				Object valorResult = doModbusLoop(memoria);

				if (newValue == null || valorResult != newValue) {
					oldValue = newValue;
					newValue = valorResult;

					if (listener != null) {
						listener.onCollect(ModbusResult.of(oldValue, newValue), thing);
					}
				}
			}
		}
		SleepUtil.sleepMillis(500);
	}

	private Object doModbusLoop(MemoriaControlador memoria) {
		try {

			if (memoria.getType() == CollectType.COIL) {
				boolean[] readCoils = this.protocolDevice.readCoils(
						((Controlador) memoria.getOwner()).getSlaveAddress(), memoria.getRegisterAddress(), 1);
				return readCoils[0];
			} else {
				int[] analogInputs = this.protocolDevice.readInputRegisters(
						((Controlador) memoria.getOwner()).getSlaveAddress(), memoria.getRegisterAddress(), 1);
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

	public void setModbusProtocolDeviceService(ModbusProtocolDeviceService modbusService) {
		this.protocolDevice = modbusService;
	}

	public void unsetModbusProtocolDeviceService() {
		this.protocolDevice = null;
	}

	private Properties getModbusProperties(Controlador controlador) {

		Properties prop = new Properties();

		prop.setProperty("connectionType", controlador.getModbusProtocol());
		prop.setProperty("slaveAddr", String.valueOf(controlador.getSlaveAddress()));
		prop.setProperty("ethport", String.valueOf(controlador.getPort()));
		prop.setProperty("ipAddress", controlador.getIp());
		prop.setProperty("respTimeout", String.valueOf(controlador.getTimeOut()));

		return prop;
	}

}
