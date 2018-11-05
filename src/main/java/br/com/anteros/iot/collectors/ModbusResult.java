package br.com.anteros.iot.collectors;

import javax.json.JsonObjectBuilder;

public class ModbusResult implements CollectResult {

	protected Object oldValue;
	protected Object newValue;

	@Override
	public Object getValue() {
		return this;
	}

	@Override
	public JsonObjectBuilder toJson(JsonObjectBuilder builder) {
		builder.add("oldValue", oldValue == null ? "" : oldValue.toString()).add("newValue",
				newValue == null ? "" : newValue.toString());
		return builder;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	private ModbusResult(Object oldValue, Object newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public static ModbusResult of(Object oldValue, Object newValue) {
		return new ModbusResult(oldValue, newValue);
	}

	@Override
	public String getValueAsString() {
		if (newValue==null)
			return "";
		return newValue.toString();
	}
}
