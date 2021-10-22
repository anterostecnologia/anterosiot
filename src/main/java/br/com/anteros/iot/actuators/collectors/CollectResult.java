package br.com.anteros.iot.actuators.collectors;

import javax.json.JsonObjectBuilder;

public interface CollectResult {
	
	public Object getValue();

	public Object getImage();
	
	public JsonObjectBuilder toJson(JsonObjectBuilder builder);

	public String getValueAsString();

	public String getImageAsString();

}
