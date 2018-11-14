package br.com.anteros.iot;

import javax.json.JsonObject;

public class Action {

	private Thing thing;
	private Part part;
	private String action;
	private String message;
	private String[] topics;
	private JsonObject receivedPayload;

	public Action(Thing thing, Part part, String action, String message, String[] topics, JsonObject receivedPayload) {
		this.thing = thing;
		this.part = part;
		this.action = action;
		this.message = message;
		this.topics = topics;
		this.receivedPayload = receivedPayload;
	}

	public static Action of(Thing thing, Part part, String action, String message, String[] topics) {
		return new Action(thing, part, action, message, topics, null);
	}
	
	public static Action of(Thing thing, Part part, JsonObject recivedPayload) {
		return new Action(thing, part, null, null, null, recivedPayload);
	}
	
	public Thing getThing() {
		return thing;
	}

	public void setThing(Thing thing) {
		this.thing = thing;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	public JsonObject getReceivedPayload() {
		return receivedPayload;
	}

	public void setReceivedPayload(JsonObject receivedPayload) {
		this.receivedPayload = receivedPayload;
	}

}
