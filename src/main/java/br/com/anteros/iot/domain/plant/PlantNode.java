package br.com.anteros.iot.domain.plant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.plant.Plant;

@JsonTypeName(DomainConstants.PLANT)
public class PlantNode extends PlantItemNode {		

	public PlantNode() {
		super();
	}

	public PlantNode(String itemId, String description) {
		super(itemId, description);
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return child.equals(PlaceNode.class);
	}

	@Override
	public Thing getInstanceOfThing() {
		return null;
	}

	@JsonIgnore
	public Plant getInstanceOfPlant() {
		return Plant.of(this);
		
	}

}
