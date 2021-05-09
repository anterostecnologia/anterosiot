package br.com.anteros.iot.domain;

public abstract class ControllerNode extends ThingNode {
	
	protected String primarySSID;
	protected String primaryPassword;
	protected String secondarySSID;
	protected String secondaryPassword;
	protected String hostNtp;
	protected String timezoneNtp;
	protected String hostMqtt;
	protected int portMqtt;
	protected String userMqtt;
	protected String passwordMqtt;
	
	
	public ControllerNode() {
		super();
	}

	public ControllerNode(String itemName, String description) {
		super(itemName, description);
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
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

	public String getHostNtp() {
		return hostNtp;
	}

	public void setHostNtp(String hostNtp) {
		this.hostNtp = hostNtp;
	}

	public String getTimezoneNtp() {
		return timezoneNtp;
	}

	public void setTimezoneNtp(String timezoneNtp) {
		this.timezoneNtp = timezoneNtp;
	}

	public String getHostMqtt() {
		return hostMqtt;
	}

	public void setHostMqtt(String hostMqtt) {
		this.hostMqtt = hostMqtt;
	}

	public int getPortMqtt() {
		return portMqtt;
	}

	public void setPortMqtt(int portMqtt) {
		this.portMqtt = portMqtt;
	}

	public String getUserMqtt() {
		return userMqtt;
	}

	public void setUserMqtt(String userMqtt) {
		this.userMqtt = userMqtt;
	}

	public String getPasswordMqtt() {
		return passwordMqtt;
	}

	public void setPasswordMqtt(String passwordMqtt) {
		this.passwordMqtt = passwordMqtt;
	}

}