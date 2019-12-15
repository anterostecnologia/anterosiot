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
import br.com.anteros.iot.domain.things.config.Config;
import br.com.anteros.iot.domain.things.config.General;
import br.com.anteros.iot.domain.things.config.Hardware;
import br.com.anteros.iot.domain.things.config.Mqtt;
import br.com.anteros.iot.domain.things.config.NTP;
import br.com.anteros.iot.domain.things.config.Network;
import br.com.anteros.iot.things.DigitalKey;

@JsonTypeName(DomainConstants.DIGITAL_KEY)
public class DigitalKeyNode extends ControllerNode implements Configurable {

	protected String[] topics;
	protected AccessType accessType; 

	public DigitalKeyNode() {
		super();
	}

	public DigitalKeyNode(String itemName, String description, String[] topics) {
		super(itemName, description);
		this.topics = topics;
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new DigitalKey(this);
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
			
			hardware.setAccessType((accessType.equals(AccessType.PINCODE)?0:1));

			if (this.getPrimarySSID() != null) {
				network.setSsid(this.getPrimarySSID());
				network.setPswd(this.getPrimaryPassword());
			} else {
				network.setSsid(deviceNode.getPrimarySSID());
				network.setPswd(deviceNode.getPrimaryPassword());
			}

			general.setPlace(getPlaceNode().getItemName());

			if (this.getHostMqtt() != null) {
				mqtt.setHost(this.getHostMqtt());
				mqtt.setPort(this.getPortMqtt());
				mqtt.setUser(this.getUserMqtt());
				mqtt.setPswd(this.getPasswordMqtt());
				mqtt.setDataTopic(getTopics());
			} else {
				mqtt.setHost(deviceNode.getHostMqtt());
				mqtt.setPort(deviceNode.getPortMqtt());
				mqtt.setUser(deviceNode.getUserMqtt());
				mqtt.setPswd(deviceNode.getPasswordMqtt());
				mqtt.setDataTopic(getTopics());
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

	public AccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(AccessType accessType) {
		this.accessType = accessType;
	}

}
