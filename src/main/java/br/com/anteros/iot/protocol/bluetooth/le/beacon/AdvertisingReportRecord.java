/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package br.com.anteros.iot.protocol.bluetooth.le.beacon;

/**
 * AdvertisingReportRecord contains all the fields of a advertising record.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @since 1.3
 */
public class AdvertisingReportRecord {

    private AdvertisingReportEventType eventType;
    private AdvertisingReportAddressType addressType;
    private String address;
    private byte[] reportData;
    private int length;
    private int rssi;

    public AdvertisingReportEventType getEventType() {
        return this.eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = AdvertisingReportEventType.valueOf((byte) eventType);
    }

    public AdvertisingReportAddressType getAddressType() {
        return this.addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = AdvertisingReportAddressType.valueOf((byte) addressType);
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getReportData() {
        return this.reportData;
    }

    public void setReportData(byte[] reportData) {
        this.reportData = reportData;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
