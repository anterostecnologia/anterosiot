package br.com.anteros.iot.domain.things.config;

public class Network {

	private String primaryBSSID;
	private String primarySSID;
	private String primaryPassword;

	private String secondaryBSSID;
	private String secondarySSID;
	private String secondaryPassword;

	private String ip;
	private String subnet;
	private String gateway;
	private String dns;

	private int wmode;
	private int hide;
	private int offtime;
	private int dhcp;
	private int hasConnection;
	public String getPrimaryBSSID() {
		return primaryBSSID;
	}
	public void setPrimaryBSSID(String primaryBSSID) {
		this.primaryBSSID = primaryBSSID;
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
	public String getSecondaryBSSID() {
		return secondaryBSSID;
	}
	public void setSecondaryBSSID(String secondaryBSSID) {
		this.secondaryBSSID = secondaryBSSID;
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
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSubnet() {
		return subnet;
	}
	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getDns() {
		return dns;
	}
	public void setDns(String dns) {
		this.dns = dns;
	}
	public int getWmode() {
		return wmode;
	}
	public void setWmode(int wmode) {
		this.wmode = wmode;
	}
	public int getHide() {
		return hide;
	}
	public void setHide(int hide) {
		this.hide = hide;
	}
	public int getOfftime() {
		return offtime;
	}
	public void setOfftime(int offtime) {
		this.offtime = offtime;
	}
	public int getDhcp() {
		return dhcp;
	}
	public void setDhcp(int dhcp) {
		this.dhcp = dhcp;
	}
	public int getHasConnection() {
		return hasConnection;
	}
	public void setHasConnection(int hasConnection) {
		this.hasConnection = hasConnection;
	}

}