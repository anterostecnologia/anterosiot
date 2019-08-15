package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.PlateReader;

@JsonTypeName(DomainConstants.PLATE_READER)
public class PlateReaderNode extends ThingNode {
	

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new PlateReader(this);		
	}


}
