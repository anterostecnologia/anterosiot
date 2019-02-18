package br.com.anteros.iot.actuators.processors;

import br.com.anteros.iot.Device;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.actuators.collectors.CollectResult;

public abstract class Processor<T extends CollectResult> implements Runnable {

	protected Thing thing;
	protected Class<? extends CollectResult> collectResult;
	protected T result;
	protected Device device;

	public Processor() {
	}

	protected Processor(Class<? extends CollectResult> collectResult) {
		this.collectResult = collectResult;
	}

	public abstract boolean isSupportedThing(Thing thing);

	public Thing getThing() {
		return thing;
	}

	public void setThings(Thing thing) {
		this.thing = thing;
	}

	public Processor<T> getProcessorsByThing(Thing thing) {
		if (this.isSupportedThing(thing)) {
			return this;
		}
		return null;
	}

	public Class<? extends CollectResult> getCollectResult() {
		return collectResult;
	}

	public void setCollectResult(Class<? extends CollectResult> collectResult) {
		this.collectResult = collectResult;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Device getDevice() {
		return device;
	}

	@Override
	public String toString() {
		return "Processor [thing=" + thing + ", device=" + device + "]";
	}
	
	
}
