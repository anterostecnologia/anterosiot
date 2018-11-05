package br.com.anteros.iot.things;

import br.com.anteros.iot.collectors.CollectResult;

public interface Publishable {
	
	public String[] getTopicsToPublishValue(CollectResult dataCollected);

}
