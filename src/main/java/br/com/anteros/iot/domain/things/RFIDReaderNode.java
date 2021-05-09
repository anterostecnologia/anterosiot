package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.Configurable;
import br.com.anteros.iot.domain.ControllerNode;
import br.com.anteros.iot.domain.DeviceMasterNode;
import br.com.anteros.iot.domain.DeviceNode;
import br.com.anteros.iot.domain.DeviceSlaveNode;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.domain.things.config.General;
import br.com.anteros.iot.domain.things.config.Mqtt;
import br.com.anteros.iot.domain.things.config.NTP;
import br.com.anteros.iot.domain.things.config.Network;
import br.com.anteros.iot.domain.things.config.RFID.Config;
import br.com.anteros.iot.domain.things.config.RFID.Hardware;
import br.com.anteros.iot.domain.things.config.RFID.Notification;
import br.com.anteros.iot.things.RFIDReader;

@JsonTypeName(DomainConstants.RFID_NODE)
public class RFIDReaderNode extends ControllerNode implements Configurable {

	protected String[] topics;
	protected RFIDModel model;
	protected int sspin;
	protected int rfidgain;
	protected int wgd0pin;
	protected int wgd1pin;
	protected long readInterval;
	protected int readMode;
	
	protected int ledEnabled;
	protected int buzzerEnabled;
	protected int displayEnabled;
	protected String firstPlaceholderMessage;
	protected String secondPlaceholderMessage;
	protected long ledOnTime;
	protected long buzzerOnTime;
	protected long displayMessageTime;
	

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
			Notification notification = new Notification();
			General general = new General();
			Mqtt mqtt = new Mqtt();
			NTP ntp = new NTP();

			if (this.getPrimarySSID() != null) {
				network.setPrimarySSID(this.getPrimarySSID());
				network.setPrimaryPassword(this.getPrimaryPassword());
			} else {
				if (deviceNode instanceof DeviceMasterNode) {
					network.setPrimarySSID(((DeviceMasterNode)deviceNode).getSsid());
					network.setPrimaryPassword(((DeviceMasterNode)deviceNode).getPassword());
				} else if (deviceNode instanceof DeviceSlaveNode) {
					network.setPrimarySSID(((DeviceSlaveNode)deviceNode).getPrimarySSID());
					network.setPrimaryPassword(((DeviceSlaveNode)deviceNode).getPrimaryPassword());
				}				
			}
			
			if (this.getSecondarySSID() != null) {
				network.setSecondarySSID(this.getSecondarySSID());
				network.setSecondaryPassword(this.getSecondaryPassword());
			} else {
				if (deviceNode instanceof DeviceMasterNode) {
					network.setSecondarySSID(((DeviceMasterNode)deviceNode).getSsidAP());
					network.setSecondaryPassword(((DeviceMasterNode)deviceNode).getPasswordAP());
				} else if (deviceNode instanceof DeviceSlaveNode) {
					network.setSecondarySSID(((DeviceSlaveNode)deviceNode).getSecondarySSID());
					network.setSecondaryPassword(((DeviceSlaveNode)deviceNode).getSecondaryPassword());
				}				
			}

			if (this.getModel().equals(RFIDModel.RC522)) {
				hardware.setReaderType(0);
				hardware.setSspin(sspin);
				hardware.setRfidgain(rfidgain);
			} else if (this.getModel().equals(RFIDModel.PN532)) {
				hardware.setReaderType(2);
				hardware.setSspin(sspin);
			} else {
				hardware.setReaderType(1);
				hardware.setWgd0pin(wgd0pin);
				hardware.setWgd1pin(wgd1pin);
			}
			
			hardware.setReadInterval(readInterval);
			hardware.setReadMode(readMode);
			
			notification.setBuzzerEnabled(buzzerEnabled);
			notification.setBuzzerOnTime(buzzerOnTime);
			notification.setDisplayEnabled(displayEnabled);
			notification.setDisplayMessageTime(displayMessageTime);
			notification.setFirstPlaceholderMessage(firstPlaceholderMessage);
			notification.setSecondPlaceholderMessage(secondPlaceholderMessage);
			notification.setLedEnabled(ledEnabled);
			notification.setLedOnTime(ledOnTime);

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

			Config config = new Config(command, network, hardware, notification, general, mqtt, ntp);

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

	public int getRfidgain() {
		return rfidgain;
	}

	public void setRfidgain(int rfidgain) {
		this.rfidgain = rfidgain;
	}

	public int getWgd0pin() {
		return wgd0pin;
	}

	public void setWgd0pin(int wgd0pin) {
		this.wgd0pin = wgd0pin;
	}

	public int getWgd1pin() {
		return wgd1pin;
	}

	public void setWgd1pin(int wgd1pin) {
		this.wgd1pin = wgd1pin;
	}

	public long getReadInterval() {
		return readInterval;
	}

	public void setReadInterval(long readInterval) {
		this.readInterval = readInterval;
	}

	public int getReadMode() {
		return readMode;
	}

	public void setReadMode(int readMode) {
		this.readMode = readMode;
	}

	public int getLedEnabled() {
		return ledEnabled;
	}

	public void setLedEnabled(int ledEnabled) {
		this.ledEnabled = ledEnabled;
	}

	public int getBuzzerEnabled() {
		return buzzerEnabled;
	}

	public void setBuzzerEnabled(int buzzerEnabled) {
		this.buzzerEnabled = buzzerEnabled;
	}

	public int getDisplayEnabled() {
		return displayEnabled;
	}

	public void setDisplayEnabled(int displayEnabled) {
		this.displayEnabled = displayEnabled;
	}

	public String getFirstPlaceholderMessage() {
		return firstPlaceholderMessage;
	}

	public void setFirstPlaceholderMessage(String firstPlaceholderMessage) {
		this.firstPlaceholderMessage = firstPlaceholderMessage;
	}

	public String getSecondPlaceholderMessage() {
		return secondPlaceholderMessage;
	}

	public void setSecondPlaceholderMessage(String secondPlaceholderMessage) {
		this.secondPlaceholderMessage = secondPlaceholderMessage;
	}

	public long getLedOnTime() {
		return ledOnTime;
	}

	public void setLedOnTime(long ledOnTime) {
		this.ledOnTime = ledOnTime;
	}

	public long getBuzzerOnTime() {
		return buzzerOnTime;
	}

	public void setBuzzerOnTime(long buzzerOnTime) {
		this.buzzerOnTime = buzzerOnTime;
	}

	public long getDisplayMessageTime() {
		return displayMessageTime;
	}

	public void setDisplayMessageTime(long displayMessageTime) {
		this.displayMessageTime = displayMessageTime;
	}
}
