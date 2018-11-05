package br.com.anteros.iot.actuators;

public class Running {
	protected Boolean running;
	
	public Running(Boolean running) {
		this.running = running;
	}
	
	public static Running of(Boolean running) {
		return new Running(running);
	}

	public Boolean isRunning() {
		return running;
	}

	public void setRunning(Boolean running) {
		this.running = running;
	}	
	
}
