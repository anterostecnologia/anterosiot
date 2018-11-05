package br.com.anteros.iot.collectors;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

public class TelemetryResult implements TelemetryCollectResult {
	protected JsonObject value;
	private TelemetryType telemetryType;

	TelemetryResult(JsonObject value, TelemetryType telemetryType){
		this.value = value;
		this.telemetryType = telemetryType;
	}
	
	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public JsonObjectBuilder toJson(JsonObjectBuilder builder) {
		Json.createObjectBuilder(value);
		return builder.addAll(Json.createObjectBuilder(value));
	}

	@Override
	public String getValueAsString() {
		Map<String, Boolean> config = new HashMap<>();

		config.put(JsonGenerator.PRETTY_PRINTING, true);

		JsonWriterFactory writerFactory = Json.createWriterFactory(config);

		String jsonString="";	
		try (Writer writer = new StringWriter()) {
			writerFactory.createWriter(writer).write(value);
			jsonString = writer.toString();
		} catch (IOException e) {
		}
		return jsonString;
	}

	@Override
	public TelemetryType getTelemetryType() {
		return telemetryType;
	}

}
