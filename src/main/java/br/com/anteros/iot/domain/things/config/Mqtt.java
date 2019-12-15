package br.com.anteros.iot.domain.things.config;

public class Mqtt {

	private String enabled;

	private String host;

	private int port;

	private String[] dataTopic;

	private String user;

	private String pswd;

	private String heartBeatTopic;

	private int heartBeatIntervalSeconds;

	private String errorTopic;

	private String bootTopic;

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}


	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public String getHeartBeatTopic() {
		return heartBeatTopic;
	}

	public void setHeartBeatTopic(String heartBeatTopic) {
		this.heartBeatTopic = heartBeatTopic;
	}

	public int getHeartBeatIntervalSeconds() {
		return heartBeatIntervalSeconds;
	}

	public void setHeartBeatIntervalSeconds(int heartBeatIntervalSeconds) {
		this.heartBeatIntervalSeconds = heartBeatIntervalSeconds;
	}

	public String getErrorTopic() {
		return errorTopic;
	}

	public void setErrorTopic(String errorTopic) {
		this.errorTopic = errorTopic;
	}

	public String getBootTopic() {
		return bootTopic;
	}

	public void setBootTopic(String bootTopic) {
		this.bootTopic = bootTopic;
	}

	public String[] getDataTopic() {
		return dataTopic;
	}

	public void setDataTopic(String[] dataTopic) {
		this.dataTopic = dataTopic;
	}

}
