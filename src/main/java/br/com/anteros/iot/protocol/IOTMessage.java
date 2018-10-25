package br.com.anteros.iot.protocol;

public class IOTMessage {
	
	private String action;
	private String part;
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

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}


}
