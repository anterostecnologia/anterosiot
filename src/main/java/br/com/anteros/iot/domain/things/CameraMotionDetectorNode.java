package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.CameraMotionDetector;

@JsonTypeName(DomainConstants.CAMERA_MOTION_DETECTOR)
public class CameraMotionDetectorNode extends ThingNode {

	protected String[] topics;
	protected String url;
	
	@Override
	public String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException {
		return mapper.writeValueAsString(node);
	}

	@Override
	public String getThingType() {
		return DomainConstants.CAMERA_MOTION_DETECTOR;
	}

	public CameraMotionDetectorNode() {
		super();
	}

	public CameraMotionDetectorNode(String itemName, String description, String[] topics, String url) {
		super(itemName, description);
		this.topics = topics;
		this.url = url;
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new CameraMotionDetector(this);
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
