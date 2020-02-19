package br.com.anteros.iot.domain;

import br.com.anteros.core.utils.ReflectionUtils;

public abstract class DeviceSlaveNode extends DeviceNode {
	
	protected String primarySSID;
	protected String primaryPassword;
	protected String secondarySSID;
	protected String secondaryPassword;	


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

	public String getPrimarySSID() {
		return primarySSID;
	}

	public void setPrimarySSID(String primarySSID) {
		this.primarySSID = primarySSID;
	}

	public String getPrimaryPassword() {
		return primaryPassword;
	}

	public void setPrimaryPassword(String primaryPassword) {
		this.primaryPassword = primaryPassword;
	}

	public String getSecondarySSID() {
		return secondarySSID;
	}

	public void setSecondarySSID(String secondarySSID) {
		this.secondarySSID = secondarySSID;
	}

	public String getSecondaryPassword() {
		return secondaryPassword;
	}

	public void setSecondaryPassword(String secondaryPassword) {
		this.secondaryPassword = secondaryPassword;
	}

}
