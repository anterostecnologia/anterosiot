package br.com.anteros.iot.domain.things;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.anteros.iot.Thing;
import br.com.anteros.iot.domain.DomainConstants;
import br.com.anteros.iot.domain.ThingNode;
import br.com.anteros.iot.things.CameraQRCodeReader;

@JsonTypeName(DomainConstants.CAMERA_QR_CODE)
public class CameraQRCodeReaderNode extends ThingNode {
	
	protected String[] topics;
	protected int intervalToReadSameQrCode = 3000;

	public int getIntervalToReadSameQrCode() {
		return intervalToReadSameQrCode;
	}

	public void setIntervalToReadSameQrCode(int intervalToReadSameQrCode) {
		this.intervalToReadSameQrCode = intervalToReadSameQrCode;
	}

	public CameraQRCodeReaderNode() {
		super();
	}

	public CameraQRCodeReaderNode(String itemName, String description, String[] topics, int intervalToReadSameQrCode) {
		super(itemName, description);
		this.topics = topics;
		this.intervalToReadSameQrCode= intervalToReadSameQrCode;
	}

	@Override
	protected boolean acceptThisTypeOfChild(Class<?> child) {
		return false;
	}

	@Override
	public Thing getInstanceOfThing() {
		return new CameraQRCodeReader(this);
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}
}
