package br.com.anteros.iot;

public class Action {

	private Thing thing;
	private Part part;
	private String action;
	private String message;
	private String[] topics;

	public Action(Thing thing, Part part, String action, String message, String[] topics) {
		this.thing = thing;
		this.part = part;
		this.action = action;
		this.message = message;
		this.topics = topics;
	}
	
	public static Action of(Thing thing, Part part, String action, String message, String[] topics) {
		return new Action(thing, part, action, message, topics);
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

}
