package br.com.anteros.iot.app.listeners;

import br.com.anteros.iot.triggers.Trigger;

public interface AnterosIOTServiceListener {

	public void onFireTrigger(Trigger source, Object value);
}
