package br.com.anteros.iot.domain.things.config.RFID;

public class Notification {
	
	private int ledEnabled;
	private int buzzerEnabled;
	private int displayEnabled;
	private String firstPlaceholderMessage;
	private String secondPlaceholderMessage;
	private long ledOnTime;
	private long buzzerOnTime;
	private long displayMessageTime;
	
	public int getLedEnabled() {
		return ledEnabled;
	}
	public void setLedEnabled(int ledEnabled) {
		this.ledEnabled = ledEnabled;
	}
	public int getBuzzerEnabled() {
		return buzzerEnabled;
	}
	public void setBuzzerEnabled(int buzzerEnabled) {
		this.buzzerEnabled = buzzerEnabled;
	}
	public int getDisplayEnabled() {
		return displayEnabled;
	}
	public void setDisplayEnabled(int displayEnabled) {
		this.displayEnabled = displayEnabled;
	}
	public String getFirstPlaceholderMessage() {
		return firstPlaceholderMessage;
	}
	public void setFirstPlaceholderMessage(String firstPlaceholderMessage) {
		this.firstPlaceholderMessage = firstPlaceholderMessage;
	}
	public String getSecondPlaceholderMessage() {
		return secondPlaceholderMessage;
	}
	public void setSecondPlaceholderMessage(String secondPlaceholderMessage) {
		this.secondPlaceholderMessage = secondPlaceholderMessage;
	}
	public long getLedOnTime() {
		return ledOnTime;
	}
	public void setLedOnTime(long ledOnTime) {
		this.ledOnTime = ledOnTime;
	}
	public long getBuzzerOnTime() {
		return buzzerOnTime;
	}
	public void setBuzzerOnTime(long buzzerOnTime) {
		this.buzzerOnTime = buzzerOnTime;
	}
	public long getDisplayMessageTime() {
		return displayMessageTime;
	}
	public void setDisplayMessageTime(long displayMessageTime) {
		this.displayMessageTime = displayMessageTime;
	}
}
