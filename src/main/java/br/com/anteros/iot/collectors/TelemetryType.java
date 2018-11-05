package br.com.anteros.iot.collectors;

public enum TelemetryType {

	ALL(""),
	HARDWARE("hardware"),
	MEMORY("memory"),
	JAVA("java"),
	NETWORK("network"),
	SO("so"),
	PLATFORM("platform"),
	CODEC("codec"),
	CLOCK("clock"),
	TEMPERATURE("temperature");
	
	private String valueType;

	TelemetryType(String valueType) {
		this.valueType = valueType;
	}

	public String getValueType() {
		return valueType;
	}
}
