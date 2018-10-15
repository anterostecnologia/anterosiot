package br.com.anteros.iot.collectors;

public interface CollectResult {
	
	public Object getValue();
	
	public String toJson();

}
