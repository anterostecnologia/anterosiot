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

import br.com.anteros.iot.protocol.ble.exceptions.BLECommandException;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeAdapter;

/**
 * BluetoothLeBeaconAdvertising allows to manage the advertising mechanism for Bluetooth LE beacons.
 *
 * @noimplement This interface is not intended to be implemented by clients.
 * @since 1.3
 */
public interface BluetoothLeBeaconAdvertiser<T extends BluetoothLeBeacon> {

    /**
     * Start Beacon advertising.
     * If the advertising has been already started or an error is detected, this method throws a
     * KuraBluetoothCommandException.
     * 
     * @throw KuraBluetoothCommandException
     */
    public void startBeaconAdvertising() throws BLECommandException;

    /**
     * Stop Beacon advertising.
     * If the advertising has been already stopped or an error is detected, this method throws a
     * KuraBluetoothCommandException.
     *
     * @throw KuraBluetoothCommandException
     */
    public void stopBeaconAdvertising() throws BLECommandException;

    /**
     * Set the minimum and maximum Beacon advertising interval.
     * Both intervals are computed as Nx0.625 ms, where N can vary from 14 to 65534.
     * So, the minimum and maximum intervals are in the range 8.75ms - 40.9s.
     * Note that further limitations can be introduced by the hardware Bluetooth controller.
     *
     * @param min
     *            Minimum time interval between advertises
     * @param max
     *            Maximum time interval between advertises
     *
     * @throw KuraBluetoothCommandException
     */
    public void updateBeaconAdvertisingInterval(Integer min, Integer max) throws BLECommandException;

    /**
     * Set the data in to the Beacon advertising packet.
     *
     * @param beacon
     *            An instance of BluetoothLeBeacon class
     *
     * @throw KuraBluetoothCommandException
     */
    public void updateBeaconAdvertisingData(T beacon) throws BLECommandException;

    /**
     * Get the bluetooth adapter this advertiser is associated to.
     * 
     * @return BluetoothLeAdapter
     */
    public BluetoothLeAdapter getAdapter();
}
