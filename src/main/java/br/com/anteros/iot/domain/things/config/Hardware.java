package br.com.anteros.iot.domain.things.config;

public class Hardware {

	private int readerType;
	private int wgd0pin;
	private int wgd1pin;
	private int sspin;
	private int rfidgain;
	private int rtype;
	private int rpin;
	private int rtime;
	private int accessType;
	private int pincode;
	private String passphrase;
	

	public int getReaderType() {
		return readerType;
	}

	public void setReaderType(int readerType) {
		this.readerType = readerType;
	}

	public int getWgd0pin() {
		return wgd0pin;
	}

	public void setWgd0pin(int wgd0pin) {
		this.wgd0pin = wgd0pin;
	}

	public int getWgd1pin() {
		return wgd1pin;
	}

	public void setWgd1pin(int wgd1pin) {
		this.wgd1pin = wgd1pin;
	}

	public int getSspin() {
		return sspin;
	}

	public void setSspin(int sspin) {
		this.sspin = sspin;
	}

	public int getRfidgain() {
		return rfidgain;
	}

	public void setRfidgain(int rfidgain) {
		this.rfidgain = rfidgain;
	}

	public int getRtype() {
		return rtype;
	}

	public void setRtype(int rtype) {
		this.rtype = rtype;
	}

	public int getRpin() {
		return rpin;
	}

	public void setRpin(int rpin) {
		this.rpin = rpin;
	}

	public int getRtime() {
		return rtime;
	}

	public void setRtime(int rtime) {
		this.rtime = rtime;
	}

	public int getAccessType() {
		return accessType;
	}

	public void setAccessType(int accessType) {
		this.accessType = accessType;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

}
