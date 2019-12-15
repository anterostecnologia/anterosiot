package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.Configurable;
import br.com.anteros.iot.domain.ControllerNode;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.domain.things.config.Config;
import br.com.anteros.iot.domain.things.config.General;
import br.com.anteros.iot.domain.things.config.Hardware;
import br.com.anteros.iot.domain.things.config.Mqtt;
import br.com.anteros.iot.domain.things.config.NTP;
import br.com.anteros.iot.domain.things.config.Network;
import br.com.anteros.iot.things.RFIDReader;

@JsonTypeName(DomainConstants.RFID_NODE)
public class RFIDReaderNode extends ControllerNode implements Configurable {

	protected String[] topics;
	protected RFIDModel model;
	protected int sspin;

	public RFIDReaderNode() {
		super();
	}

	public RFIDReaderNode(String itemName, String description, String[] topics, RFIDModel model) {
		super(itemName, description);
		this.topics = topics;
		this.model = model;
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new RFIDReader(this);
	}

	@Override
	public String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException {
		DeviceNode deviceNode = getDeviceNode();
		if (deviceNode != null) {
			String command = "configfile";
			Network network = new Network();
			Hardware hardware = new Hardware();
			General general = new General();
			Mqtt mqtt = new Mqtt();
			NTP ntp = new NTP();

			if (this.getPrimarySSID() != null) {
				network.setSsid(this.getPrimarySSID());
				network.setPswd(this.getPrimaryPassword());
			} else {
				network.setSsid(deviceNode.getPrimarySSID());
				network.setPswd(deviceNode.getPrimaryPassword());
			}

			if (this.getModel().equals(RFIDModel.RC522)) {
				hardware.setReaderType(4);
			} else {
				hardware.setReaderType(2);
			}
			hardware.setSspin(sspin);

			general.setPlace(getPlaceNode().getItemName());

			if (this.getHostMqtt() != null) {
				mqtt.setHost(this.getHostMqtt());
				mqtt.setPort(this.getPortMqtt());
				mqtt.setUser(this.getUserMqtt());
				mqtt.setPswd(this.getPasswordMqtt());
			} else {
				mqtt.setHost(deviceNode.getHostMqtt());
				mqtt.setPort(deviceNode.getPortMqtt());
				mqtt.setUser(deviceNode.getUserMqtt());
				mqtt.setPswd(deviceNode.getPasswordMqtt());
			}

			if (this.getHostNtp() != null) {
				ntp.setServer(this.getHostNtp());
				ntp.setTimezone(this.getTimezoneNtp());
			} else {
				ntp.setServer(deviceNode.getHostNtp());
				ntp.setTimezone(deviceNode.getTimezoneNtp());
			}

			Config config = new Config(command, network, hardware, general, mqtt, ntp);

			return mapper.writeValueAsString(config);
		}

		return null;

	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	public RFIDModel getModel() {
		return model;
	}

	public void setModel(RFIDModel model) {
		this.model = model;
	}

	public int getSspin() {
		return sspin;
	}

	public void setSspin(int sspin) {
		this.sspin = sspin;
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

	public int getTimezoneNtp() {
		return timezoneNtp;
	}

	public void setTimezoneNtp(int timezoneNtp) {
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
