package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PartNode;
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

}
