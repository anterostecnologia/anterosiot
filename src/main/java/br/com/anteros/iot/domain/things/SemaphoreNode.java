package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PartNode;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.Semaphore;

@JsonTypeName(DomainConstants.SEMAPHORE)
public class SemaphoreNode extends ThingNode {	

	public SemaphoreNode() {
		super();
	}

	public SemaphoreNode(String itemName, String description) {
		super(itemName, description);
	}


	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {		
		return child.equals(PartNode.class) || ReflectionUtils.isExtendsClass(PartNode.class, child);
	}

	@Override
	public Thing getInstanceOfThing() {
		return new Semaphore(this);
	}

	@Override
	public String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException {
		return mapper.writeValueAsString(node);
	}

	@Override
	public String getThingType() {
		return DomainConstants.SEMAPHORE;
	}

}
