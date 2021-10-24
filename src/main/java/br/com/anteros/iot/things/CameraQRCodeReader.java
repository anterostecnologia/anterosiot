package br.com.anteros.iot.things;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.anteros.core.utils.ArrayUtils;
import br.com.anteros.iot.DeviceController;
import br.com.anteros.iot.Part;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.domain.things.CameraQRCodeReaderNode;
import br.com.anteros.iot.plant.PlantItem;
import br.com.anteros.iot.triggers.Trigger;

public class CameraQRCodeReader extends PlantItem implements Thing, Publishable {

	protected DeviceController deviceController;
	protected boolean needsPropagation;
	protected String[] topics;
	protected Set<Trigger> triggers = new HashSet<>();
	protected int intervalToReadSameQrCode = 5000;
	protected Object userData = "TAG_PEDESTRE";
	protected String status;
	protected String lastValue;

	public int getIntervalToReadSameQrCode() {
		return intervalToReadSameQrCode;
	}

	public void setIntervalToReadSameQrCode(int intervalToReadSameQrCode) {
		this.intervalToReadSameQrCode = intervalToReadSameQrCode;
	}

	public CameraQRCodeReader(PlantItemNode node) {
		this.loadConfiguration(node);
	}

	public String getThingID() {
		return itemId;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	@Override
	public String getLastValue() {
		return lastValue;
	}

	@Override
	public void setLastValue(String value) {
		this.lastValue = value;
	}

	@Override
	public Object getUserData() {
		return userData;
	}

	@Override
	public void setUserData(Object data){
		this.userData = data;
	}

	@Override
	public String getThingType() {
		return DomainConstants.CAMERA_QR_CODE;
	}

	public Set<Part> getParts() {
		return Collections.unmodifiableSet(new HashSet<>());
	}

	public boolean hasParts() {
		return false;
	}

	public Thing addPart(Part part) {
		return this;
	}

	public Thing removePart(Part part) {
		return this;
	}

	public Part getPartById(String part) {
		return null;
	}

	@Override
	public Thing loadConfiguration(PlantItemNode node) {
		this.itemId = node.getItemName();
		this.description = node.getDescription();
		this.intervalToReadSameQrCode = ((CameraQRCodeReaderNode) node).getIntervalToReadSameQrCode();
		this.topics = ((CameraQRCodeReaderNode) node).getTopics();
		return this;
	}

	@Override
	protected boolean acceptThisTypeOfPlantItem(Class<?> child) {
		return false;
	}

	public DeviceController getDeviceController() {
		return deviceController;
	}

	public void setDeviceController(DeviceController deviceController) {
		this.deviceController = deviceController;
	}

	@Override
	public String[] getTopicsToPublishValue(CollectResult collectedData) {
		if (topics == null || topics.length == 0) {
			return new String[] { this.getPath() };
		}
		return topics;
	}

	@Override
	public Trigger[] getTriggers() {
		return triggers.toArray(new Trigger[] {});
	}

	@Override
	public Thing addTrigger(Trigger trigger) {
		triggers.add(trigger);
		return this;
	}

	@Override
	public Thing removeTrigger(Trigger trigger) {
		triggers.remove(trigger);
		return this;
	}

	@Override
	public String[] getActions() {
		return null;
	}

	@Override
	public String toString() {
		return "CameraQRCodeReader [topics=" + Arrays.toString(topics) + ", intervalToReadSameQrCode="
				+ intervalToReadSameQrCode + ", itemId=" + itemId + ", description=" + description + "]";
	}
	
	@Override
	public boolean needsPropagation() {
		return needsPropagation ? true : false;
	}

}
