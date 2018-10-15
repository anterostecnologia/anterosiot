package br.com.anteros.iot.plant.exceptions;

public class PlantException extends RuntimeException {

	public PlantException() {
		super();
	}

	public PlantException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PlantException(String message, Throwable cause) {
		super(message, cause);
	}

	public PlantException(String message) {
		super(message);
	}

	public PlantException(Throwable cause) {
		super(cause);
	}

}
