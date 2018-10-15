package br.com.anteros.iot;

import br.com.anteros.iot.collectors.CollectorListener;

public abstract class Collector implements Actuable {

	protected CollectorListener listener;
	protected Thing thing;

	public Collector() {

	}

	public Collector(CollectorListener listener, Thing thing) {
		this.listener = listener;
		this.thing = thing;
	}

	public abstract void startCollect();
	
	public abstract void stopCollect();

	public abstract boolean isSupportedThing(Thing thing);

	public CollectorListener getListener() {
		return listener;
	}

	public void setListener(CollectorListener listener) {
		this.listener = listener;
	}

	public Thing getThing() {
		return thing;
	}

	public void setThing(Thing thing) {
		this.thing = thing;
	}

	protected Collector createNewInstance(CollectorListener listener, Thing thing) throws Exception {
		Collector result = this.getClass().newInstance();
		result.setListener(listener);
		result.setThing(thing);
		return result;
	}

}
