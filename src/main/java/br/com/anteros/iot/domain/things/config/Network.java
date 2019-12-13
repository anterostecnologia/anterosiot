package br.com.anteros.iot.domain.things.config;

public class Network {

	private String bssid;
	private String ssid;
	private String pswd;

	private String secondaryBssi;
	private String secondarySsid;
	private String secondaryPswd;

	private String ip;
	private String subnet;
	private String gateway;
	private String dns;

	private int wmode;
	private int hide;
	private int offtime;
	private int dhcp;
	private int hasConnection;

	public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public String getSecondaryBssi() {
		return secondaryBssi;
	}

	public void setSecondaryBssi(String secondaryBssi) {
		this.secondaryBssi = secondaryBssi;
	}

	public String getSecondarySsid() {
		return secondarySsid;
	}

	public void setSecondarySsid(String secondarySsid) {
		this.secondarySsid = secondarySsid;
	}

	public String getSecondaryPswd() {
		return secondaryPswd;
	}

	public void setSecondaryPswd(String secondaryPswd) {
		this.secondaryPswd = secondaryPswd;
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