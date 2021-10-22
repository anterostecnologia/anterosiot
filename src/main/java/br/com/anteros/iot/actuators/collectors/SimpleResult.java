package br.com.anteros.iot.actuators.collectors;

import javax.json.JsonObjectBuilder;

public class SimpleResult implements CollectResult {
	
	protected Object value;

	protected Object image;
	
	public SimpleResult(Object value) {
		super();
		this.value = value;
		this.image = null;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public Object getImage() {
		return null;
	}

	@Override
	public JsonObjectBuilder toJson(JsonObjectBuilder builder) {
		builder.add("value",value+"");
		if (image !=null) {
			builder.add("image", image + "");
		}
		return builder;
	}

	@Override
	public String getValueAsString() {
		if (value==null)
			return "";
		return value.toString();
	}

	@Override
	public String getImageAsString() {
		if (image==null)
			return "";
		return image.toString();
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setImage(Object image) {
		this.image = image;
	}

}
