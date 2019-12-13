package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.Configurable;
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
public class RFIDReaderNode extends ThingNode implements Configurable {
	
	protected String[] topics;
	protected RFIDModel model;
	protected int sspin;

	public RFIDReaderNode() {
		super();
	}

	public RFIDReaderNode(String itemName, String description, String[] topics,RFIDModel model) {
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
		if (this.model.equals(RFIDModel.PN532)) {
			
			DeviceNode deviceNode = getDeviceNode();
			if (deviceNode != null) {
				String command = "configfile";
				Network network = new Network();
				Hardware hardware = new Hardware();
				General general = new General();
				Mqtt mqtt = new Mqtt();
				NTP ntp = new NTP();
				
				network.setSsid(deviceNode.getSsid());
				network.setPswd(deviceNode.getPassword());
				
				hardware.setReaderType(2);
				hardware.setSspin(sspin);
				
				general.setPlace(getPlaceNode().getItemName());
				
				mqtt.setHost(deviceNode.getHostMqtt());
				mqtt.setPort(deviceNode.getPortMqtt());
				mqtt.setUser(deviceNode.getUserMqtt());
				mqtt.setPswd(deviceNode.getPasswordMqtt());
				
				ntp.setServer(deviceNode.getHostNtp());
				ntp.setTimezone(deviceNode.getTimezoneNtp());
				
				Config config = new Config(command, network, hardware, general, mqtt, ntp);
				
				return mapper.writeValueAsString(config);
			}
		
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
}
