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
import br.com.anteros.iot.domain.things.config.General;
import br.com.anteros.iot.domain.things.config.Mqtt;
import br.com.anteros.iot.domain.things.config.NTP;
import br.com.anteros.iot.domain.things.config.Network;
import br.com.anteros.iot.domain.things.config.RFID.Config;
import br.com.anteros.iot.things.DeviceScan;

@JsonTypeName(DomainConstants.DEVICE_SCAN)
public class DeviceScanNode extends ControllerNode implements Configurable {

	protected String[] topics;

	public DeviceScanNode() {
		super();
	}

	public DeviceScanNode(String itemName, String description, String[] topics) {
		super(itemName, description);
		this.topics = topics;
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new DeviceScan(this);
	}

	@Override
	public String parseConfig(ObjectMapper mapper, PlantItemNode node) throws JsonProcessingException {
		DeviceNode deviceNode = getDeviceNode();
		if (deviceNode != null) {
			String command = "configfile";
			Network network = new Network();
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

			Config config = new Config(command, network, null, null, general, mqtt, ntp);

			return mapper.writeValueAsString(config);
		}

		return null;

	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topic) {
		this.topics = topic;
	}

}
