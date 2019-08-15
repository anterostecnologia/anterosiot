package br.com.anteros.iot.domain;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.actions.ActionNode;
import br.com.anteros.iot.domain.devices.MasterAsusTinkerNode;
import br.com.anteros.iot.domain.devices.MasterComputerNode;
import br.com.anteros.iot.domain.devices.MasterDeviceRPiNode;
import br.com.anteros.iot.domain.devices.SlaveAsusTinkerNode;
import br.com.anteros.iot.domain.devices.SlaveComputerNode;
import br.com.anteros.iot.domain.devices.SlaveRPiNode;
import br.com.anteros.iot.domain.plant.PlaceNode;
import br.com.anteros.iot.domain.plant.PlantNode;
import br.com.anteros.iot.domain.things.BarrierGateNode;
import br.com.anteros.iot.domain.things.BarrierSensorNode;
import br.com.anteros.iot.domain.things.BeaconNode;
import br.com.anteros.iot.domain.things.CameraMotionDetectorNode;
import br.com.anteros.iot.domain.things.CameraQRCodeReaderNode;
import br.com.anteros.iot.domain.things.EletronicGateNode;
import br.com.anteros.iot.domain.things.EletronicLockNode;
import br.com.anteros.iot.domain.things.GenericRelayNode;
import br.com.anteros.iot.domain.things.LampOrBulbNode;
import br.com.anteros.iot.domain.things.MagneticLockNode;
import br.com.anteros.iot.domain.things.PlcNode;
import br.com.anteros.iot.domain.things.PresenceDetectorNode;
import br.com.anteros.iot.domain.things.RFIDReaderNode;
import br.com.anteros.iot.domain.things.RingStripLED12Node;
import br.com.anteros.iot.domain.things.SemaphoreNode;
import br.com.anteros.iot.domain.things.TemperatureOneWireNode;
import br.com.anteros.iot.domain.things.parts.GreenLEDSemaphorePartNode;
import br.com.anteros.iot.domain.things.parts.MemoryPlcNode;
import br.com.anteros.iot.domain.things.parts.RedLEDSemaphorePartNode;
import br.com.anteros.iot.things.GenericRelay;
import br.com.anteros.iot.things.sensors.BarrierSensor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@id")
@JsonSubTypes(value = { @Type(value = PlantNode.class, name = "plant"),
		@Type(value = BarrierGateNode.class, name = "barrierGate"),
		@Type(value = EletronicGateNode.class, name = "eletronicGate"),
		@Type(value = EletronicLockNode.class, name = "eletronicLock"),
		@Type(value = BeaconNode.class, name = "beacon"),
		@Type(value = BarrierSensorNode.class, name = "barrierSensor"),
		@Type(value = GreenLEDSemaphorePartNode.class, name = "greenLedSemaphore"),
		@Type(value = RedLEDSemaphorePartNode.class, name = "redLedSemaphore"),
		@Type(value = LampOrBulbNode.class, name = "lamp"),
		@Type(value = MasterAsusTinkerNode.class, name = "masterAsusTinker"),
		@Type(value = MasterDeviceRPiNode.class, name = "masterRPi"), @Type(value = PlaceNode.class, name = "place"),
		@Type(value = CameraQRCodeReaderNode.class, name = "cameraQRCode"),
		@Type(value = RFIDReaderNode.class, name = "rfidNode"), @Type(value = SemaphoreNode.class, name = "semaphore"),
		@Type(value = SlaveAsusTinkerNode.class, name = "slaveAsusTinker"),
		@Type(value = MagneticLockNode.class, name = "magneticLock"),
		@Type(value = TemperatureOneWireNode.class, name = "temperatureOneWire"),
		@Type(value = PresenceDetectorNode.class, name = "presenceDetector"),
		@Type(value = SlaveRPiNode.class, name = "slaveRPi"),
		@Type(value = PlcNode.class, name = "controladorModBus"),
		@Type(value = MemoryPlcNode.class, name = "memoriaControlador"),
		@Type(value = MasterComputerNode.class, name = "masterComputer"),
		@Type(value = SlaveComputerNode.class, name = "slaveComputer"),
		@Type(value = CameraMotionDetectorNode.class, name = "cameraMotionDetector"),
		@Type(value = GenericRelayNode.class, name = "genericRelay"),
		@Type(value = ActionNode.class, name = "action"),
		@Type(value = RingStripLED12Node.class, name = DomainConstants.RING_STRIP_LED12)})
public abstract class PlantItemNode {

	protected String itemName;

	protected String description;

	@JsonIdentityReference
	@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@id")
	protected PlantItemNode itemNodeOwner;

	@JsonIdentityReference
	@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@id")
	protected PlantItemNode controllerOwner;

	@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@id")
	protected Set<PlantItemNode> items = new LinkedHashSet<>();

	protected abstract boolean acceptThisTypeOfChild(Class<?> child);

	@JsonIgnore
	public abstract Thing getInstanceOfThing();

	public PlantItemNode() {

	}

	public PlantItemNode(String itemName, String description) {
		this.itemName = itemName;
		this.description = description;
	}

	public PlantItemNode getItemNodeOwner() {
		return itemNodeOwner;
	}

	public void setItemNodeOwner(PlantItemNode itemNodeOwner) {
		this.itemNodeOwner = itemNodeOwner;
	}

	public Set<PlantItemNode> getItems() {
		return Collections.unmodifiableSet(items);
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PlantItemNode addChild(PlantItemNode child) {
		if (acceptThisTypeOfChild(child.getClass())) {
			child.setItemNodeOwner(this);
			this.items.add(child);
		}
		return this;
	}

	public PlantItemNode addChildren(PlantItemNode... children) {
		for (PlantItemNode child : children) {
			if (acceptThisTypeOfChild(child.getClass())) {
				child.setItemNodeOwner(this);
				this.items.add(child);
			}
		}
		return this;
	}

	public PlantItemNode removeChild(PlantItemNode child) {
		this.items.remove(child);
		return this;
	}

	public PlantItemNode findNodeByName(String name) {
		if (this.itemName != null && this.itemName.equalsIgnoreCase(name)) {
			return this;
		}
		for (PlantItemNode itemNode : this.getItems()) {
			PlantItemNode result = itemNode.findNodeByName(name);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	public PlantItemNode getControllerOwner() {
		return controllerOwner;
	}

	public void setControllerOwner(PlantItemNode controllerOwner) {
		this.controllerOwner = controllerOwner;
	}

	public PlantItemNode findNodesByType(Class<?> type, List<PlantItemNode> result) {
		if (ReflectionUtils.isExtendsClass(type, this.getClass())) {
			result.add(this);
		}
		for (PlantItemNode itemNode : this.getItems()) {
			PlantItemNode item = itemNode.findNodesByType(type, result);
			if (item != null) {
				result.add(this);
			}
		}
		return null;
	}

}