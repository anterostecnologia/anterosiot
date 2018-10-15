package br.com.anteros.iot.plant;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.plant.PlaceNode;
import br.com.anteros.iot.domain.plant.PlantNode;
import br.com.anteros.iot.plant.exceptions.PlantException;

public class Plant extends PlantItem {
	
	protected Plant(PlantNode node) {
		loadConfiguration(node);
	}

	public static Plant of(PlantNode node) {
		return new Plant(node);
	}
	
	public String getPath() {
		return itemId;
	}

	public String getPlantId() {
		return itemId;
	}
	
	private void loadConfiguration(PlantNode node) {
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		for (PlantItemNode child : node.getItems()) {
			if (!(child instanceof PlaceNode)) {
				throw new PlantException("Item filho "+child.getItemName()+" "+child.getDescription()+" na configuração da Planta deve ser um local.");
			}
			PlaceNode placeNode = (PlaceNode) child;
			this.addItems(placeNode.getInstanceoOfPlace());
		}
		
	}

	@Override
	protected boolean acceptThisTypeOfPlantItem(Class<?> child) {
		return (child == Place.class || ReflectionUtils.isExtendsClass(Place.class, child));
	}

	
	

}
