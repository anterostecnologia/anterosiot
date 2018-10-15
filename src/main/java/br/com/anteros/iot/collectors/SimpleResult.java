package br.com.anteros.iot.collectors;

public class SimpleResult implements CollectResult {
	
	protected Object value;
	
	public SimpleResult(Object value) {
		super();
		this.value = value;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String toJson() {
		return "{ 'value' : "+value+" }";
	}

}
