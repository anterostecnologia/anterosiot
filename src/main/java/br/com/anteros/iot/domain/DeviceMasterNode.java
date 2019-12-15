package br.com.anteros.iot.domain;

import br.com.anteros.core.utils.ReflectionUtils;

public abstract class DeviceMasterNode extends DeviceNode {
	
	protected String ssid;
	protected String password;
	protected String ssidAP;
	protected String passwordAP;

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

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSsidAP() {
		return ssidAP;
	}

	public void setSsidAP(String ssidAP) {
		this.ssidAP = ssidAP;
	}

	public String getPasswordAP() {
		return passwordAP;
	}

	public void setPasswordAP(String passwordAP) {
		this.passwordAP = passwordAP;
	}

}
