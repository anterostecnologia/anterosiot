package br.com.anteros.iot.app.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.triggers.Trigger;

public interface AnterosIOTServiceListener {

	public void onFireTrigger(Trigger source, Object value);
	
	public void onAddSubTypeNames(ObjectMapper mapper);
}
