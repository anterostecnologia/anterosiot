package br.com.anteros.iot.plant;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.Device;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.plant.PlaceNode;

public class Place extends PlantItem {

	public Place(PlaceNode node) {
		loadConfiguration(node);
	}

	private void loadConfiguration(PlaceNode node) {
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		for (PlantItemNode child : node.getItems()) {
			if (child instanceof PlaceNode) {
				this.addItems(((PlaceNode) child).getInstanceoOfPlace());
			}
		}
	}

	@Override
	protected boolean acceptThisTypeOfPlantItem(Class<?> child) {
		return (child == Place.class || ReflectionUtils.isExtendsClass(Place.class, child)
				|| ReflectionUtils.isImplementsInterface(child, Device.class)
				|| ReflectionUtils.isExtendsClass(Thing.class, child));
	}

}
