package br.com.anteros.iot.plant;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class PlantItem {

	protected PlantItem itemOwner;

	protected Set<PlantItem> items = new LinkedHashSet<>();

	protected String itemId;

	protected String description;
	
	protected abstract boolean acceptThisTypeOfPlantItem(Class<?> child);

	public PlantItem getItemOwner() {
		return itemOwner;
	}

	public void setItemOwner(PlantItem itemOwner) {
		this.itemOwner = itemOwner;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<PlantItem> getItems() {
		return items;
	}

	public PlantItem addItems(PlantItem... items) {
		for (PlantItem item : items) {
			if (this.acceptThisTypeOfPlantItem(item.getClass())) {
				item.setItemOwner(this);
				this.items.add(item);
			}
		}
		return this;
	}

	public PlantItem removeItem(PlantItem item) {
		this.items.remove(item);
		return this;
	}
	
	public String getPath() {
		if (this.itemOwner==null) {
			return itemId;
		}
		return itemOwner.getPath() + "/" + itemId;
	}

	public PlantItem getItemByName(String itemId) {
		if (this.itemId.equals(itemId)) {
			return this;
		}
		for (PlantItem item : items) {
			if (item.getItemId().equals(itemId)) {
				return item;
			}
			PlantItem result = item.getItemByName(itemId);
			if (result!=null) {
				return result;
			}
		}
		return null;		
	}

	@Override
	public String toString() {
		return "PlantItem [itemId=" + itemId + ", description=" + description + " -> itemOwner=" + itemOwner + "]";
	}

}
