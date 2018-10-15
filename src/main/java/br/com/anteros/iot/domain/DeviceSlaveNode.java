package br.com.anteros.iot.domain;

import br.com.anteros.core.utils.ReflectionUtils;

public abstract class DeviceSlaveNode extends DeviceNode {

	public DeviceSlaveNode() {
		super();
	}

	public DeviceSlaveNode(String itemName, String description) {
		super(itemName, description);
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return child.equals(ThingNode.class) || ReflectionUtils.isExtendsClass(ThingNode.class, child);
	}

}
