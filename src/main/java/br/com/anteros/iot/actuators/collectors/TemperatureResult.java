package br.com.anteros.iot.actuators.collectors;

import javax.json.JsonObjectBuilder;

public class TemperatureResult implements CollectResult {

	protected Double oldTemperature;
	protected Double newTemperature;

	protected TemperatureResult(Double oldTemperature, Double newTemperature) {
		this.oldTemperature = oldTemperature;
		this.newTemperature = newTemperature;
	}

	public static TemperatureResult of(Double oldTemperature, Double newTemperature) {
		return new TemperatureResult(oldTemperature, newTemperature);
	}

	public Double getOldTemperature() {
		return oldTemperature;
	}

	public void setOldTemperature(Double oldTemperature) {
		this.oldTemperature = oldTemperature;
	}

	public Double getNewTemperature() {
		return newTemperature;
	}

	public void setNewTemperature(Double newTemperature) {
		this.newTemperature = newTemperature;
	}

	@Override
	public Object getValue() {
		return this;
	}

	@Override
	public Object getImage() {
		return null;
	}

	@Override
	public JsonObjectBuilder toJson(JsonObjectBuilder builder) {
		builder.add("oldTemperature",oldTemperature).add("newTemperature",newTemperature);
		return builder;
		
	}

	@Override
	public String getValueAsString() {
		if (newTemperature==null)
			return "";
		return newTemperature.toString();
	}

	@Override
	public String getImageAsString() {
		return null;
	}

}
