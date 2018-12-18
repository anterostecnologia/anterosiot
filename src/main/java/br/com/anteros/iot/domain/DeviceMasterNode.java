package br.com.anteros.iot.domain;

import br.com.anteros.core.utils.ReflectionUtils;

public abstract class DeviceMasterNode extends DeviceNode {

	public DeviceMasterNode() {
		super();
	}

	public DeviceMasterNode(String itemName, String description) {
		super(itemName, description);
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return child.equals(DeviceSlaveNode.class) || ReflectionUtils.isExtendsClass(DeviceSlaveNode.class, child)
				|| ReflectionUtils.isExtendsClass(ThingNode.class, child);
	}

}
