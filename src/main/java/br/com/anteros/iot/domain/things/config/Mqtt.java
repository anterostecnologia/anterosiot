package br.com.anteros.iot.domain.things.config;

public class Mqtt {

	private int enabled;

	private String host;

	private int port;

	private String dataTopic;

	private String user;

	private String pswd;

	private String heartBeatTopic;

	private long heartBeatIntervalSeconds;

	private String errorTopic;

	private String bootTopic;

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
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

	public String getDataTopic() {
		return dataTopic;
	}

	public void setDataTopic(String dataTopic) {
		this.dataTopic = dataTopic;
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

	public long getHeartBeatIntervalSeconds() {
		return heartBeatIntervalSeconds;
	}

	public void setHeartBeatIntervalSeconds(long heartBeatIntervalSeconds) {
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

}
