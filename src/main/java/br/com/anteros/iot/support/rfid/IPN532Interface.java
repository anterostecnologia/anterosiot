package br.com.anteros.iot.support.rfid;

public interface IPN532Interface {
  
  static final byte PN532_PREAMBLE = 0x00;
  static final byte PN532_STARTCODE1 = 0x00;
  static final byte PN532_STARTCODE2 = (byte) 0xFF;
  static final byte PN532_POSTAMBLE = 0x00;

  static final byte PN532_HOSTTOPN532 = (byte) 0xD4;
  static final byte PN532_PN532TOHOST = (byte) 0xD5;

	public abstract void begin() throws Exception;

	public abstract void wakeup() throws Exception;

	public abstract CommandStatus writeCommand(byte[] header, byte[] body)
			throws InterruptedException;

	public abstract CommandStatus writeCommand(byte header[]) throws InterruptedException;

	public abstract int readResponse(byte[] buffer, int expectedLength,
			int timeout) throws InterruptedException;

	public abstract int readResponse(byte[] buffer, int expectedLength)
			throws InterruptedException;
	

}