package br.com.anteros.iot.support.serial;

public interface SerialIOCallbacks {
	void connected(boolean b);
	void onSerialData(byte b);
	void onSerialData(byte[] ba, int len);
}
