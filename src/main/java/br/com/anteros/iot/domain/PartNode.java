package br.com.anteros.iot.domain;

public abstract class PartNode extends ThingNode {
	
	public PartNode() {
		super();
	}

	public PartNode(String itemName, String description) {
		super(itemName, description);
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

}
