package br.com.anteros.iot.protocol.modbus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Properties;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.io.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.anteros.iot.protocol.modbus.type.ModbusProtocolErrorCode;

public class ModbusProtocolDevice implements ModbusProtocolDeviceService {
	
	private static final Logger s_logger = LoggerFactory.getLogger(ModbusProtocolDevice.class);

    private ConnectionFactory m_connectionFactory;

    static final String PROTOCOL_NAME = "modbus";
    public static final String PROTOCOL_CONNECTION_TYPE_ETHER_RTU = "TCP-RTU";
    public static final String PROTOCOL_CONNECTION_TYPE_ETHER_TCP = "TCP/IP";
    private int m_respTout;
    private int m_txMode;
    private boolean m_connConfigd = false;
    private boolean m_protConfigd = false;
    private String m_connType = null;
    private Communicate m_comm;    
    private static int transactionIndex = 0;
	
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.m_connectionFactory = connectionFactory;
    }

    public void unsetConnectionFactory(ConnectionFactory connectionFactory) {
        this.m_connectionFactory = null;
    }



    // ----------------------------------------------------------------
    //
    // Activation APIs
    //
    // ----------------------------------------------------------------
    protected void activate(ComponentContext componentContext) {
        s_logger.info("activate...");
    }

    protected void deactivate(ComponentContext componentContext) {
        s_logger.info("deactivate...");
        try {
            disconnect();
        } catch (ModbusProtocolException e) {
            s_logger.error("ModbusProtocolException :  {}", e.getCode());
        }
    }

    /**
     * two connection types are available:
     * <ul>
     * <li> serial mode (PROTOCOL_CONNECTION_TYPE_SERIAL)
     * 
     * <li> Ethernet with 2 possible modes : RTU over TCP/IP (PROTOCOL_CONNECTION_TYPE_ETHER_RTU) or real MODBUS-TCP/IP
     * (PROTOCOL_CONNECTION_TYPE_ETHER_TCP). 
     * <ul>
     * <p>
     * <h4>PROTOCOL_CONNECTION_TYPE_SERIAL</h4>
     * see {@link org.eclipse.kura.comm.CommConnection CommConnection} package for more detail.
     * <table border="1">
     * <tr>
     * <th>Key</th>
     * <th>Description</th>
     * </tr>
     * <tr>
     * <td>connectionType</td>
     * <td>"RS232" (from PROTOCOL_CONNECTION_TYPE_SERIAL). This parameter indicates the connection type for the
     * configuration. See {@link org.eclipse.kura.comm.CommConnection CommConnection} for more details on serial port
     * configuration.
     * </tr>
     * <tr>
     * <td>port</td>
     * <td>the actual device port, such as "/dev/ttyUSB0" in linux</td>
     * </tr>
     * <tr>
     * <td>baudRate</td>
     * <td>baud rate to be configured for the port</td>
     * </tr>
     * <tr>
     * <td>stopBits</td>
     * <td>number of stop bits to be configured for the port</td>
     * </tr>
     * <tr>
     * <td>parity</td>
     * <td>parity mode to be configured for the port</td>
     * </tr>
     * <tr>
     * <td>bitsPerWord</td>
     * <td>only RTU mode supported, bitsPerWord must be 8</td>
     * </tr>
     * </table>
     * <p>
     * <h4>PROTOCOL_CONNECTION_TYPE_ETHER_TCP</h4>
     * The Ethernet mode merely opens a socket and sends the full RTU mode Modbus packet over that socket connection and
     * expects to receive a full RTU mode Modbus response, including the CRC bytes.
     * <table border="1">
     * <tr>
     * <th>Key</th>
     * <th>Description</th>
     * </tr>
     * <tr>
     * <td>connectionType</td>
     * <td>"ETHERTCP" (from PROTOCOL_CONNECTION_TYPE_ETHER_TCP). This parameter indicates the connection type for the
     * configurator.
     * </tr>
     * <tr>
     * <td>ipAddress</td>
     * <td>the 4 octet IP address of the field device (xxx.xxx.xxx.xxx)</td>
     * </tr>
     * <tr>
     * <td>port</td>
     * <td>port on the field device to connect to</td>
     * </tr>
     * </table>
     */
    @Override
    public void configureConnection(Properties connectionConfig) throws ModbusProtocolException {
        if ((this.m_connType = connectionConfig.getProperty("connectionType")) == null) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_CONFIGURATION);
        }

        String txMode;
        String respTimeout;
        if (this.m_protConfigd || (txMode = connectionConfig.getProperty("transmissionMode")) == null
                || (respTimeout = connectionConfig.getProperty("respTimeout")) == null) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_CONFIGURATION);
        }
        if (txMode.equals(ModbusTransmissionMode.RTU)) {
            this.m_txMode = ModbusTransmissionMode.RTU_MODE;
        } else if (txMode.equals(ModbusTransmissionMode.ASCII)) {
            this.m_txMode = ModbusTransmissionMode.ASCII_MODE;
        } else {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_CONFIGURATION);
        }
        this.m_respTout = Integer.parseInt(respTimeout);
        if (this.m_respTout < 0) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_CONFIGURATION);
        }
        this.m_protConfigd = true;

        if (this.m_connConfigd) {
            this.m_comm.disconnect();
            this.m_comm = null;
            this.m_connConfigd = false;
        }

        if (PROTOCOL_CONNECTION_TYPE_ETHER_TCP.equals(this.m_connType)
                || PROTOCOL_CONNECTION_TYPE_ETHER_RTU.equals(this.m_connType)) {
            this.m_comm = new EthernetCommunicate(this.m_connectionFactory, connectionConfig);
        } else {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_CONFIGURATION);
        }

        this.m_connConfigd = true;
    }

    /**
     * get the name "modbus" for this protocol
     * 
     * @return "modbus"
     */
    @Override
    public String getProtocolName() {
        return "modbus";
    }

    @Override
    public void connect() throws ModbusProtocolException {
        if (!this.m_connConfigd) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_CONFIGURATION);
        }
        this.m_comm.connect();
    }

    @Override
    public void disconnect() throws ModbusProtocolException {
        if (this.m_connConfigd) {
            this.m_comm.disconnect();
            this.m_comm = null;
            this.m_connConfigd = false;
            s_logger.info("Serial comm disconnected");
        }
        this.m_protConfigd = false;
    }

    @Override
    public int getConnectStatus() {
        if (!this.m_connConfigd) {
            return ConnectionStatus.NEVERCONNECTED;
        }
        return this.m_comm.getConnectStatus();
    }

    /**
     * The only constructor must be the configuration mechanism
     */
    abstract private class Communicate {

        abstract public void connect();

        abstract public void disconnect() throws ModbusProtocolException;

        abstract public int getConnectStatus();

        abstract public byte[] msgTransaction(byte[] msg) throws ModbusProtocolException;
    }

    /**
     * Installation of an ethernet connection to communicate
     */
    private final class EthernetCommunicate extends Communicate {

        InputStream inputStream;
        OutputStream outputStream;
        Socket socket;
        int port;
        String ipAddress;
        String connType;
        boolean connected = false;

        public EthernetCommunicate(ConnectionFactory connFactory, Properties connectionConfig)
                throws ModbusProtocolException {
            s_logger.debug("Configure TCP connection");
            String sPort;
            this.connType = connectionConfig.getProperty("connectionType");

            if ((sPort = connectionConfig.getProperty("ethport")) == null
                    || (this.ipAddress = connectionConfig.getProperty("ipAddress")) == null) {
                throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_CONFIGURATION);
            }
            this.port = Integer.valueOf(sPort).intValue();
            ModbusProtocolDevice.this.m_connConfigd = true;
            this.socket = new Socket();
        }

        @Override
        public void connect() {
            if (!ModbusProtocolDevice.this.m_connConfigd) {
                s_logger.error("Can't connect, port not configured");
            } else {
                if (!this.connected) {
                    try {
                        this.socket = new Socket();
                        this.socket.connect(new InetSocketAddress(this.ipAddress, this.port), 
                            ModbusProtocolDevice.this.m_respTout);
                        try {
                            this.inputStream = this.socket.getInputStream();
                            this.outputStream = this.socket.getOutputStream();
                            this.connected = true;
                            s_logger.info("TCP connected");
                        } catch (IOException e) {
                            disconnect();
                            s_logger.error("Failed to get socket streams: " + e);
                        }
                    } catch (IOException e) {
                        this.socket = null;
                        s_logger.error("Failed to connect to remote: " + e);
                    }
                }
            }
        }

        @Override
        public void disconnect() {
            if(this.socket==null){
                return;
            }
            if (ModbusProtocolDevice.this.m_connConfigd) {
                if (this.connected) {
                    try {
                        if (!this.socket.isInputShutdown()) {
                            this.socket.shutdownInput();
                        }
                        if (!this.socket.isOutputShutdown()) {
                            this.socket.shutdownOutput();
                        }
                        this.socket.close();
                    } catch (IOException eClose) {
                        s_logger.error("Error closing TCP: " + eClose);
                    }
                    this.inputStream = null;
                    this.outputStream = null;
                    this.connected = false;
                    this.socket = null;
                }
            }
        }

        @Override
        public int getConnectStatus() {
            if (this.connected) {
                return ConnectionStatus.CONNECTED;
            } else if (ModbusProtocolDevice.this.m_connConfigd) {
                return ConnectionStatus.DISCONNECTED;
            } else {
                return ConnectionStatus.NEVERCONNECTED;
            }
        }

        @Override
        public byte[] msgTransaction(byte[] msg) throws ModbusProtocolException {
            byte[] cmd = null;

            // ---------------------------------------------- Send Message
            // ---------------------------------------------------
            if (ModbusProtocolDevice.this.m_txMode == ModbusTransmissionMode.RTU_MODE) {
                if (PROTOCOL_CONNECTION_TYPE_ETHER_TCP.equals(this.connType)) {
                    cmd = new byte[msg.length + 6];
                    // build MBAP header
                    int index = getNextTransactionIndex();
                    cmd[0] = (byte) (index >> 8);
                    cmd[1] = (byte) index;
                    cmd[2] = 0;
                    cmd[3] = 0;
                    // length
                    int len = msg.length;
                    cmd[4] = (byte) (len >> 8);
                    cmd[5] = (byte) len;
                    for (int i = 0; i < msg.length; i++) {
                        cmd[i + 6] = msg[i];
                    }
                    // No crc in Modbus TCP
                } else {
                    cmd = new byte[msg.length + 2];
                    for (int i = 0; i < msg.length; i++) {
                        cmd[i] = msg[i];
                    }
                    // Add crc calculation to end of message
                    int crc = Crc16.getCrc16(msg, msg.length, 0x0ffff);
                    cmd[msg.length] = (byte) crc;
                    cmd[msg.length + 1] = (byte) (crc >> 8);
                }
            } else {
                throw new ModbusProtocolException(ModbusProtocolErrorCode.METHOD_NOT_SUPPORTED,
                        "Only RTU over TCP/IP supported");
            }

            // Check connection status and connect
            connect();
            if (!this.connected) {
                throw new ModbusProtocolException(ModbusProtocolErrorCode.TRANSACTION_FAILURE,
                        "Cannot transact on closed socket");
            }

            // Send the message
            try {
                // flush input
                while (this.inputStream.available() > 0) {
                    this.inputStream.read();
                }
                // send all data
                this.outputStream.write(cmd, 0, cmd.length);
                this.outputStream.flush();
            } catch (IOException e) {
                // Assume this means the socket is closed...make sure it is
                s_logger.error("Socket disconnect in send: " + e);
                disconnect();
                throw new ModbusProtocolException(ModbusProtocolErrorCode.TRANSACTION_FAILURE, "Send failure: "
                        + e.getMessage());
            }

            // ---------------------------------------------- Receive response
            // ---------------------------------------------------
            // wait for and process response

            boolean endFrame = false;
            byte[] response = new byte[262]; // response buffer
            int respIndex = 0;
            int minimumLength = 5; // default minimum message length
            if (PROTOCOL_CONNECTION_TYPE_ETHER_TCP.equals(this.connType)) {
                minimumLength += 6;
            }
            while (!endFrame) {
                try {
                    this.socket.setSoTimeout(ModbusProtocolDevice.this.m_respTout);
                    int resp = this.inputStream.read(response, respIndex, 1);
                    if (resp > 0) {
                        respIndex += resp;
                        if (PROTOCOL_CONNECTION_TYPE_ETHER_TCP.equals(this.connType)) {
                            if (respIndex == 7) {
                                // test modbus id
                                if (response[6] != msg[0]) {
                                    throw new ModbusProtocolException(ModbusProtocolErrorCode.TRANSACTION_FAILURE,
                                            "incorrect modbus id " + String.format("%02X", response[6]));
                                }
                            } else if (respIndex == 8) {
                                // test function number
                                if ((response[7] & 0x7f) != msg[1]) {
                                    throw new ModbusProtocolException(ModbusProtocolErrorCode.TRANSACTION_FAILURE,
                                            "incorrect function number " + String.format("%02X", response[7]));
                                }
                            } else if (respIndex == 9) {
                                // Check first for an Exception response
                            	if ((response[7] & 0x80) == 0x80) {                                    
                            		throw new ModbusProtocolException(ModbusProtocolErrorCode.TRANSACTION_FAILURE,
                            				"Modbus responds an error = " + String.format("%02X", response[8]));
                                } else {
                                    if (response[7] == ModbusFunctionCodes.FORCE_SINGLE_COIL
                                            || response[7] == ModbusFunctionCodes.PRESET_SINGLE_REG
                                            || response[7] == ModbusFunctionCodes.FORCE_MULTIPLE_COILS
                                            || response[7] == ModbusFunctionCodes.PRESET_MULTIPLE_REGS) {
                                        minimumLength = 12;
                                    } else {
                                        // bytes count
                                        minimumLength = (response[8] & 0xff) + 9;
                                    }
                                }
                            } else if (respIndex == minimumLength) {
                                endFrame = true;
                            }
                        } else {

                        }
                    } else {
                        s_logger.error("Socket disconnect in recv");
                        throw new ModbusProtocolException(ModbusProtocolErrorCode.TRANSACTION_FAILURE, "Recv failure");
                    }
                } catch (SocketTimeoutException e) {
                    String failMsg = "Recv timeout";
                    s_logger.warn(failMsg);
                    throw new ModbusProtocolException(ModbusProtocolErrorCode.TRANSACTION_FAILURE, failMsg);
                } catch (IOException e) {
                    s_logger.error("Socket disconnect in recv: " + e);
                    throw new ModbusProtocolException(ModbusProtocolErrorCode.TRANSACTION_FAILURE, "Recv failure");
                }

            }

            // then check for a valid message
            switch (response[7]) {
            case ModbusFunctionCodes.FORCE_SINGLE_COIL:
            case ModbusFunctionCodes.PRESET_SINGLE_REG:
            case ModbusFunctionCodes.FORCE_MULTIPLE_COILS:
            case ModbusFunctionCodes.PRESET_MULTIPLE_REGS:
                byte[] ret = new byte[8];
                for (int i = 6; i < 12; i++) {
                    ret[i - 6] = response[i];
                }
                return ret;
            case ModbusFunctionCodes.READ_COIL_STATUS:
            case ModbusFunctionCodes.READ_INPUT_STATUS:
            case ModbusFunctionCodes.READ_INPUT_REGS:
            case ModbusFunctionCodes.READ_HOLDING_REGS:
                int byteCnt = (response[8] & 0xff) + 3 + 6;
                ret = new byte[byteCnt - 6];
                for (int i = 6; i < byteCnt; i++) {
                    ret[i - 6] = response[i];
                }
                return ret;
            }
            return null;
        }
    }

    @Override
    public boolean[] readCoils(int unitAddr, int dataAddress, int count) throws ModbusProtocolException {
        if (!this.m_connConfigd) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.NOT_CONNECTED);
        }

        boolean[] ret = new boolean[count];
        int index = 0;

        byte[] resp;
        /*
         * construct the command issue and get results
         */
        byte[] cmd = new byte[6];
        cmd[0] = (byte) unitAddr;
        cmd[1] = (byte) ModbusFunctionCodes.READ_COIL_STATUS;
        cmd[2] = (byte) (dataAddress / 256);
        cmd[3] = (byte) (dataAddress % 256);
        cmd[4] = (byte) (count / 256);
        cmd[5] = (byte) (count % 256);

        /*
         * send the message and get the response
         */
        resp = this.m_comm.msgTransaction(cmd);

        /*
         * process the response (address & CRC already confirmed)
         */
        if (resp.length < 3 || resp.length < (resp[2] & 0xff) + 3) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
        }
        if ((resp[2] & 0xff) == (count + 7) / 8) {
            byte mask = 1;
            int byteOffset = 3;
            for (int j = 0; j < count; j++, index++) {
                // get this point's value
                if ((resp[byteOffset] & mask) == mask) {
                    ret[index] = true;
                } else {
                    ret[index] = false;
                }
                // advance the mask and offset index
                if ((mask <<= 1) == 0) {
                    mask = 1;
                    byteOffset++;
                }
            }
        } else {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_ADDRESS);
        }

        return ret;
    }

    @Override
    public boolean[] readDiscreteInputs(int unitAddr, int dataAddress, int count) throws ModbusProtocolException {
        if (!this.m_connConfigd) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.NOT_CONNECTED);
        }

        boolean[] ret = new boolean[count];
        int index = 0;

        byte[] resp;
        /*
         * construct the command issue and get results
         */
        byte[] cmd = new byte[6];
        cmd[0] = (byte) unitAddr;
        cmd[1] = (byte) ModbusFunctionCodes.READ_INPUT_STATUS;
        cmd[2] = (byte) (dataAddress / 256);
        cmd[3] = (byte) (dataAddress % 256);
        cmd[4] = (byte) (count / 256);
        cmd[5] = (byte) (count % 256);

        /*
         * send the message and get the response
         */
        resp = this.m_comm.msgTransaction(cmd);

        /*
         * process the response (address & CRC already confirmed)
         */
        if (resp.length < 3 || resp.length < (resp[2] & 0xff) + 3) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
        }
        if ((resp[2] & 0xff) == (count + 7) / 8) {
            byte mask = 1;
            int byteOffset = 3;
            for (int j = 0; j < count; j++, index++) {
                // get this point's value
                if ((resp[byteOffset] & mask) == mask) {
                    ret[index] = true;
                } else {
                    ret[index] = false;
                }
                // advance the mask and offset index
                if ((mask <<= 1) == 0) {
                    mask = 1;
                    byteOffset++;
                }
            }
        } else {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_ADDRESS);
        }

        return ret;
    }

    @Override
    public void writeSingleCoil(int unitAddr, int dataAddress, boolean data) throws ModbusProtocolException {
        if (!this.m_connConfigd) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.NOT_CONNECTED);
        }

        byte[] resp;

        byte[] cmd = new byte[6];
        cmd[0] = (byte) unitAddr;
        cmd[1] = ModbusFunctionCodes.FORCE_SINGLE_COIL;
        cmd[2] = (byte) (dataAddress / 256);
        cmd[3] = (byte) (dataAddress % 256);
        cmd[4] = data == true ? (byte) 0xff : (byte) 0;
        cmd[5] = 0;

        /*
         * send the message and get the response
         */
        resp = this.m_comm.msgTransaction(cmd);

        /*
         * process the response
         */
        if (resp.length < 6) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
        }
        for (int i = 0; i < 6; i++) {
            if (cmd[i] != resp[i]) {
                throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
            }
        }

    }

    @Override
    public void writeMultipleCoils(int unitAddr, int dataAddress, boolean[] data) throws ModbusProtocolException {
        if (!this.m_connConfigd) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.NOT_CONNECTED);
        }

        /*
         * write multiple boolean values
         */
        int localCnt = data.length;
        int index = 0;
        byte[] resp;
        /*
         * construct the command, issue and verify response
         */
        int dataLength = (localCnt + 7) / 8;
        byte[] cmd = new byte[dataLength + 7];
        cmd[0] = (byte) unitAddr;
        cmd[1] = ModbusFunctionCodes.FORCE_MULTIPLE_COILS;
        cmd[2] = (byte) (dataAddress / 256);
        cmd[3] = (byte) (dataAddress % 256);
        cmd[4] = (byte) (localCnt / 256);
        cmd[5] = (byte) (localCnt % 256);
        cmd[6] = (byte) dataLength;

        // put the data on the command
        byte mask = 1;
        int byteOffset = 7;
        cmd[byteOffset] = 0;
        for (int j = 0; j < localCnt; j++, index++) {
            // get this point's value
            if (data[index]) {
                cmd[byteOffset] += mask;
            }
            // advance the mask and offset index
            if ((mask <<= 1) == 0) {
                mask = 1;
                byteOffset++;
                cmd[byteOffset] = 0;
            }
        }

        /*
         * send the message and get the response
         */
        resp = this.m_comm.msgTransaction(cmd);

        /*
         * process the response
         */
        if (resp.length < 6) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
        }
        for (int j = 0; j < 6; j++) {
            if (cmd[j] != resp[j]) {
                throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
            }
        }
    }

    @Override
    public int[] readHoldingRegisters(int unitAddr, int dataAddress, int count) throws ModbusProtocolException {
        if (!this.m_connConfigd) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.NOT_CONNECTED);
        }

        int[] ret = new int[count];
        int index = 0;

        byte[] resp;
        /*
         * construct the command issue and get results, putting the results
         * away at index and then incrementing index for the next command
         */
        byte[] cmd = new byte[6];
        cmd[0] = (byte) unitAddr;
        cmd[1] = (byte) ModbusFunctionCodes.READ_HOLDING_REGS;
        cmd[2] = (byte) (dataAddress / 256);
        cmd[3] = (byte) (dataAddress % 256);
        cmd[4] = 0;
        cmd[5] = (byte) count;

        /*
         * send the message and get the response
         */
        resp = this.m_comm.msgTransaction(cmd);

        /*
         * process the response (address & CRC already confirmed)
         */
        if (resp.length < 3 || resp.length < (resp[2] & 0xff) + 3) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
        }
        if ((resp[2] & 0xff) == count * 2) {
            int byteOffset = 3;
            for (int j = 0; j < count; j++, index++) {
                int val = resp[byteOffset + ModbusDataOrder.MODBUS_WORD_ORDER_BIG_ENDIAN.charAt(0) - '1'] & 0xff;
                val <<= 8;
                val += resp[byteOffset + ModbusDataOrder.MODBUS_WORD_ORDER_BIG_ENDIAN.charAt(1) - '1'] & 0xff;

                ret[index] = val;

                byteOffset += 2;
            }
        } else {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_ADDRESS);
        }
        return ret;
    }

    @Override
    public int[] readInputRegisters(int unitAddr, int dataAddress, int count) throws ModbusProtocolException {

        if (!this.m_connConfigd) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.NOT_CONNECTED);
        }

        int[] ret = new int[count];
        int index = 0;

        byte[] resp;
        /*
         * construct the command issue and get results, putting the results
         * away at index and then incrementing index for the next command
         */
        byte[] cmd = new byte[6];
        cmd[0] = (byte) unitAddr;
        cmd[1] = (byte) ModbusFunctionCodes.READ_INPUT_REGS;
        cmd[2] = (byte) (dataAddress / 256);
        cmd[3] = (byte) (dataAddress % 256);
        cmd[4] = 0;
        cmd[5] = (byte) count;

        /*
         * send the message and get the response
         */
        resp = this.m_comm.msgTransaction(cmd);

        /*
         * process the response (address & CRC already confirmed)
         */
        if (resp.length < 3 || resp.length < (resp[2] & 0xff) + 3) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
        }
        if ((resp[2] & 0xff) == count * 2) {
            int byteOffset = 3;
            for (int j = 0; j < count; j++, index++) {
                int val = resp[byteOffset + ModbusDataOrder.MODBUS_WORD_ORDER_BIG_ENDIAN.charAt(0) - '1'] & 0xff;
                val <<= 8;
                val += resp[byteOffset + ModbusDataOrder.MODBUS_WORD_ORDER_BIG_ENDIAN.charAt(1) - '1'] & 0xff;

                ret[index] = val;

                byteOffset += 2;
            }
        } else {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_ADDRESS);
        }
        return ret;
    }

    @Override
    public void writeSingleRegister(int unitAddr, int dataAddress, int data) throws ModbusProtocolException {
        if (!this.m_connConfigd) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.NOT_CONNECTED);
        }

        byte[] cmd = new byte[6];
        cmd[0] = (byte) unitAddr;
        cmd[1] = ModbusFunctionCodes.PRESET_SINGLE_REG;
        cmd[2] = (byte) (dataAddress / 256);
        cmd[3] = (byte) (dataAddress % 256);
        cmd[4] = (byte) (data >> 8);
        cmd[5] = (byte) data;

        /*
         * send the message and get the response
         */
        byte[] resp = this.m_comm.msgTransaction(cmd);

        /*
         * process the response
         */
        if (resp.length < 6) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
        }
        for (int i = 0; i < 6; i++) {
            if (cmd[i] != resp[i]) {
                throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
            }
        }
    }

    @Override
    public void writeMultipleRegister(int unitAddr, int dataAddress, int[] data) throws ModbusProtocolException {
        if (!this.m_connConfigd) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.NOT_CONNECTED);
        }

        int localCnt = data.length;
        /*
         * construct the command, issue and verify response
         */
        int dataLength = localCnt * 2;
        byte[] cmd = new byte[dataLength + 7];
        cmd[0] = (byte) unitAddr;
        cmd[1] = ModbusFunctionCodes.PRESET_MULTIPLE_REGS;
        cmd[2] = (byte) (dataAddress / 256);
        cmd[3] = (byte) (dataAddress % 256);
        cmd[4] = (byte) (localCnt / 256);
        cmd[5] = (byte) (localCnt % 256);
        cmd[6] = (byte) dataLength;

        // put the data on the command
        int byteOffset = 7;
        int index = 0;
        for (int j = 0; j < localCnt; j++, index++) {
            cmd[byteOffset + ModbusDataOrder.MODBUS_WORD_ORDER_BIG_ENDIAN.charAt(0) - '1'] = (byte) (data[index] >> 8);
            cmd[byteOffset + ModbusDataOrder.MODBUS_WORD_ORDER_BIG_ENDIAN.charAt(1) - '1'] = (byte) data[index];

            byteOffset += 2;
        }

        /*
         * send the message and get the response
         */
        byte[] resp = this.m_comm.msgTransaction(cmd);

        /*
         * process the response
         */
        if (resp.length < 6) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
        }
        for (int j = 0; j < 6; j++) {
            if (cmd[j] != resp[j]) {
                throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
            }
        }
    }

    @Override
    public boolean[] readExceptionStatus(int unitAddr) throws ModbusProtocolException {
        if (!this.m_connConfigd) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.NOT_CONNECTED);
        }

        boolean[] ret = new boolean[8];
        int index = 0;

        byte[] resp;
        /*
         * construct the command issue and get results
         */
        byte[] cmd = new byte[2];
        cmd[0] = (byte) unitAddr;
        cmd[1] = (byte) ModbusFunctionCodes.READ_EXCEPTION_STATUS;

        /*
         * send the message and get the response
         */
        resp = this.m_comm.msgTransaction(cmd);

        /*
         * process the response (address & CRC already confirmed)
         */
        if (resp.length < 3) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
        }
        byte mask = 1;
        for (int j = 0; j < 8; j++, index++) {
            // get this point's value
            if ((resp[2] & mask) == mask) {
                ret[index] = true;
            } else {
                ret[index] = false;
            }
            // advance the mask and offset index
            if ((mask <<= 1) == 0) {
                mask = 1;
            }
        }

        return ret;
    }

    @Override
    public ModbusCommEvent getCommEventCounter(int unitAddr) throws ModbusProtocolException {
        ModbusCommEvent mce = new ModbusCommEvent();
        if (!this.m_connConfigd) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.NOT_CONNECTED);
        }

        /*
         * construct the command issue and get results
         */
        byte[] cmd = new byte[2];
        cmd[0] = (byte) unitAddr;
        cmd[1] = (byte) ModbusFunctionCodes.GET_COMM_EVENT_COUNTER;

        /*
         * send the message and get the response
         */
        byte[] resp;
        resp = this.m_comm.msgTransaction(cmd);

        /*
         * process the response (address & CRC already confirmed)
         */
        if (resp.length < 6) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
        }
        int val = resp[2] & 0xff;
        val <<= 8;
        val += resp[3] & 0xff;
        mce.setStatus(val);
        val = resp[4] & 0xff;
        val <<= 8;
        val += resp[5] & 0xff;
        mce.setEventCount(val);

        return mce;
    }

    @Override
    public ModbusCommEvent getCommEventLog(int unitAddr) throws ModbusProtocolException {
        ModbusCommEvent mce = new ModbusCommEvent();
        if (!this.m_connConfigd) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.NOT_CONNECTED);
        }

        /*
         * construct the command issue and get results
         */
        byte[] cmd = new byte[2];
        cmd[0] = (byte) unitAddr;
        cmd[1] = (byte) ModbusFunctionCodes.GET_COMM_EVENT_LOG;

        /*
         * send the message and get the response
         */
        byte[] resp;
        resp = this.m_comm.msgTransaction(cmd);

        /*
         * process the response (address & CRC already confirmed)
         */
        if (resp.length < (resp[2] & 0xff) + 3 || (resp[2] & 0xff) > 64 + 7) {
            throw new ModbusProtocolException(ModbusProtocolErrorCode.INVALID_DATA_TYPE);
        }
        int val = resp[3] & 0xff;
        val <<= 8;
        val += resp[4] & 0xff;
        mce.setStatus(val);

        val = resp[5] & 0xff;
        val <<= 8;
        val += resp[6] & 0xff;
        mce.setEventCount(val);

        val = resp[7] & 0xff;
        val <<= 8;
        val += resp[8] & 0xff;
        mce.setMessageCount(val);

        int count = (resp[2] & 0xff) - 4;
        int[] events = new int[count];
        for (int j = 0; j < count; j++) {
            int bval = resp[9 + j] & 0xff;
            events[j] = bval;
        }
        mce.setEvents(events);

        return mce;
    }

    /**
     * Calculates and returns the next transaction index for Modbus TCP.
     * 
     * @return the next transaction index.
     */
    private int getNextTransactionIndex() {
        transactionIndex++;
        if (transactionIndex > 0xffff) {
            transactionIndex = 0;
        }
        return transactionIndex;
    }
}
