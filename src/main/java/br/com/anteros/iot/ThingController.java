package br.com.anteros.iot;

public interface ThingController {
	
	public void beforeStart();

	public void beforeStop();

	public void beforeRestart();

	public void afterStart();

	public void afterRestart();

	public String getThingID(); // UUID
	
	public void stop();

	public void start();

	public void pause();

	public void resume();
	
	public boolean isAvailable();
	
	public Thing getThing();

}
