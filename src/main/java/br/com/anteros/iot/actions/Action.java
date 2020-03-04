package br.com.anteros.iot.actions;

import java.util.Arrays;

import javax.json.JsonObject;

import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;

public class Action {

	private Thing thing;
	private Part part;
	private String action;
	private String message;
	private String[] topics;
	private JsonObject receivedPayload;
	private ExecutionCondition executionCondition;

	public Action(Thing thing, Part part, String action, String message, String[] topics, JsonObject receivedPayload,
			ExecutionCondition executionCondition) {
		this.thing = thing;
		this.part = part;
		this.action = action;
		this.message = message;
		this.topics = topics;
		this.receivedPayload = receivedPayload;
		this.executionCondition = executionCondition;
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
			ExecutionCondition executionCondition) {
		return new Action(thing, part, action, message, topics, null, executionCondition);
	}

	public static Action of(Thing thing, Part part, JsonObject recivedPayload) {
		return new Action(thing, part, null, null, null, recivedPayload, null);
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
		return "Action [thing=" + thing + ", part=" + part + ", action=" + action + ", message=" + message + ", topics="
				+ Arrays.toString(topics) + ", receivedPayload=" + receivedPayload + "]";
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

		// Does comparison according with condition

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
		// Get target's value of comparison
		if (executionCondition.getTarget().equals(Target.STATUS)) {
			return thing.getStatus();
		}
		return null;
	}

}
