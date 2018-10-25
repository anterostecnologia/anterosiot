package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.BarrierGate;

@JsonTypeName(DomainConstants.BARRIER_GATE)
public class BarrierGateNode extends ThingNode {

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new BarrierGate(this);		
	}

}
