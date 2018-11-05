package br.com.anteros.iot.collectors;

import javax.json.JsonObjectBuilder;

public interface CollectResult {
	
	public Object getValue();
	
	public JsonObjectBuilder toJson(JsonObjectBuilder builder);

	public String getValueAsString();

}
