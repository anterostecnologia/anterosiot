package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi4j.temperature.TemperatureScale;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.sensors.TemperatureSensorOneWire;

@JsonTypeName(DomainConstants.TEMPERATURE_ONE_WIRE)
public class TemperatureOneWireNode extends ThingNode {

	protected String sensorId;
	protected String[] topics;
	protected TemperatureScale scale;

	public TemperatureOneWireNode() {
		super();
	}

	public TemperatureOneWireNode(String itemName, String description, String sensorId, String[] topics, TemperatureScale scale) {
		super(itemName, description);
		this.topics = topics;
		this.sensorId = sensorId;
		this.scale = scale;
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new TemperatureSensorOneWire(this);
	}
	
	@Override
	public String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException {
		return mapper.writeValueAsString(node);
	}

	@Override
	public String getThingType() {
		return DomainConstants.TEMPERATURE_ONE_WIRE;
	}

	public TemperatureScale getScale() {
		return scale;
	}

	public void setScale(TemperatureScale scale) {
		this.scale = scale;
	}

}
