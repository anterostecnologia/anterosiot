package br.com.anteros.iot.collectors;

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
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		return sb.append("{").append("'oldTemperature' : ").append(oldTemperature).append(", 'newTemperature': ")
				.append(newTemperature).append("}").toString();
	}

}
