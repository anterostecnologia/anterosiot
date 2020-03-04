package br.com.anteros.iot.domain.things.config;

public class General {

	private String hostnm;
	private String place;
	private String pswd;

	private long restart;

	public String getHostnm() {
		return hostnm;
	}

	public void setHostnm(String hostnm) {
		this.hostnm = hostnm;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public long getRestart() {
		return restart;
	}

	public void setRestart(long restart) {
		this.restart = restart;
	}

}
