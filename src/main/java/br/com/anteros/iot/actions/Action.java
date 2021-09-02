package br.com.anteros.iot.actions;

import java.util.Arrays;

import javax.json.JsonObject;

import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;

public class Action {

	private Thing thing;
	private Part part;
	private String action;
	private String event;
	private String message;
	private String[] topics;
	private JsonObject receivedPayload;
	private ExecutionCondition executionCondition;

	public Action(Thing thing, Part part, String action, String message, String[] topics, JsonObject receivedPayload,
			ExecutionCondition executionCondition, String event) {
		this.thing = thing;
		this.part = part;
		this.action = action;
		this.message = message;
		this.topics = topics;
		this.receivedPayload = receivedPayload;
		this.executionCondition = executionCondition;
		this.event = event;
		configExecutionCondition();
	}

	private void configExecutionCondition() {
		if (this.receivedPayload != null) {
			JsonObject ExecutionConditionJSON = this.receivedPayload.getJsonObject("executionCondition");
			if (this.executionCondition == null) {
				this.executionCondition = new ExecutionCondition();
			}
			this.executionCondition.setValue(ExecutionConditionJSON.getString("value"));
			this.executionCondition.setConditionByString(ExecutionConditionJSON.getString("condition"));
			this.executionCondition.setTargetByString(ExecutionConditionJSON.getString("target"));
		}

	}

	public static Action of(Thing thing, Part part, String action, String message, String[] topics,
			ExecutionCondition executionCondition, String event) {
		return new Action(thing, part, action, message, topics, null, executionCondition, event);
	}

	public static Action of(Thing thing, Part part, JsonObject recivedPayload, String event) {
		String action = null;
		try {
			action = recivedPayload.getString("action");
		} catch (Exception e){
		}
		return new Action(thing, part, action, null, null, recivedPayload, null, event);
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
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

	@Override
	public String toString() {
		return "Action{" +
				"thing=" + thing +
				", part=" + part +
				", action='" + action + '\'' +
				", event='" + event + '\'' +
				", message='" + message + '\'' +
				", topics=" + Arrays.toString(topics) +
				", receivedPayload=" + receivedPayload +
				", executionCondition=" + executionCondition +
				'}';
	}

	public ExecutionCondition getExecutionCondition() {
		return executionCondition;
	}

	public void setExecutionCondition(ExecutionCondition ExecutionCondition) {
		this.executionCondition = ExecutionCondition;
	}

	public boolean canExecute() {

		if (executionCondition.getCondition().equals(Condition.NONE)) {
			return true;
		}

		String value = getConditionValue();
		if (value == null) {
			return false;
		}

		if (executionCondition.getCondition().equals(Condition.EQUAL)) {
			if (value.equalsIgnoreCase(executionCondition.getValue())) {
				return true;
			}
			return false;
		}

		if (executionCondition.getCondition().equals(Condition.NOT_EQUAL)) {
			if (!value.equalsIgnoreCase(executionCondition.getValue())) {
				return true;
			}
			return false;
		}

		return false;
	}

	private String getConditionValue() {
		if (executionCondition.getTarget().equals(Target.STATUS)) {
			return thing.getStatus();
		}
		return null;
	}

}
