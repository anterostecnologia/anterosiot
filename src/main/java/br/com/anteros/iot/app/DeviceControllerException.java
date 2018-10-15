package br.com.anteros.iot.app;

public class DeviceControllerException extends RuntimeException {

	public DeviceControllerException() {
		super();
	}

	public DeviceControllerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DeviceControllerException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeviceControllerException(String message) {
		super(message);
	}

	public DeviceControllerException(Throwable cause) {
		super(cause);
	}

}
