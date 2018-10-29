package br.com.anteros.iot.protocol.modbus;

public class ModbusTransmissionMode {
	
	private ModbusTransmissionMode() {
    };

    public static final String RTU = "RTU";
    public static final String ASCII = "ASCII";
    public static final int RTU_MODE = 0;
    public static final int ASCII_MODE = 1;
}
