package br.com.anteros.iot.domain.plant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.plant.Place;
import br.com.anteros.iot.plant.PlantItem;

@JsonTypeName(DomainConstants.PLACE)
public class PlaceNode extends PlantItemNode {

	public PlaceNode() {
		super();
	}

	public PlaceNode(String itemId, String description) {
		super(itemId, description);
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return child.equals(PlaceNode.class) || ReflectionUtils.isExtendsClass(DeviceNode.class, child) || ReflectionUtils.isExtendsClass(ThingNode.class, child);
	}

	@Override
	public Thing getInstanceOfThing() {
		return null;
	}

	@JsonIgnore
	public PlantItem getInstanceoOfPlace() {
		return new Place(this);
	}



}
