package br.com.anteros.iot.protocol;

public class IOTMessage {
	
	private String action;
	private String value;
	
	public IOTMessage() {
		
	}
	
	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "IOTMessage [action=" + action + ", value=" + value + "]";
	}
	
	
}
