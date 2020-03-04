package br.com.anteros.iot;

public interface ThingController {
	
	public String getThingID(); // UUID
	
	public void beforePause();	
	
	public void beforeStart();

	public void beforeStop();

	public void beforeRestart();
	
	public void afterResume();

	public void afterStart();

	public void afterRestart();
	
	public void stop();

	public void start();

	public void pause();

	public void resume();
	
	public boolean isAvailable();
	
	public Thing getThing();

}
