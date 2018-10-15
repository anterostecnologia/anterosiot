package br.com.anteros.iot.protocol;

public class IOTMessage {
	
	private String thing;
	private String part;
	private String action;
	
	public IOTMessage() {
		
	}
	
	public String getThing() {
		return thing;
	}


	public void setThing(String thing) {
		this.thing = thing;
	}


	public String getPart() {
		return part;
	}


	public void setPart(String part) {
		this.part = part;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "IOTMessage [thing=" + thing + ", part=" + part + ", action="
				+ action + "]";
	}	

}
