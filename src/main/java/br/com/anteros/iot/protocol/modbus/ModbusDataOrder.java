package br.com.anteros.iot.protocol.modbus;

public class ModbusDataOrder {
	
	private ModbusDataOrder() {
    };

    /**
     * booleans do not have a specified data order
     */
    public static final String MODBUS_BOOLEAN_ORDER = "none";
    /**
     * this is the Modbus default (note only 16 bit or 2 byte data)
     */
    public static final String MODBUS_WORD_ORDER_BIG_ENDIAN = "12";
    public static final String MODBUS_WORD_ORDER_LITTLE_ENDIAN = "21";
    /**
     * this is the most common 32 bit arrangement used by many devices
     */
    public static final String MODBUS_LONG_ORDER_BIG_BIG_ENDIAN = "1234";
    public static final String MODBUS_LONG_ORDER_BIG_LITTLE_ENDIAN = "2143";
    public static final String MODBUS_LONG_ORDER_LITTLE_BIG_ENDIAN = "3412";
    public static final String MODBUS_LONG_ORDER_LITTLE_LITTLE_ENDIAN = "4321";
}
