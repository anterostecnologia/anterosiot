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

import br.com.anteros.iot.protocol.ble.exceptions.BluetoothBeaconAdvertiserNotAvailable;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeAdapter;

/**
 * BluetoothLeBeaconService provides a mechanism for interfacing with specific Bluetooth LE Beacon devices.
 * It allows to advertise beacon packets and to scan for beacons of the given BluetoothLeBeacon type using the
 * configured adapter.
 *
 * @noimplement This interface is not intended to be implemented by clients.
 * @since 1.3
 */
public interface BluetoothLeBeaconService<T extends BluetoothLeBeacon> {

    /**
     * Instantiate a new scanner for beacons.
     * 
     * @param adapter
     *            the bluetooth adapter used by the scanner
     * @return BluetoothLeBeaconScanner
     */
    public BluetoothLeBeaconScanner<T> newBeaconScanner(BluetoothLeAdapter adapter);

    /**
     * Instantiate a new advertiser for beacons.
     * 
     * @param adapter
     *            the bluetooth adapter used by the advertiser
     * @return BluetoothLeBeaconAdvertiser
     * @throws BluetoothBeaconAdvertiserNotAvailable
     */
    public BluetoothLeBeaconAdvertiser<T> newBeaconAdvertiser(BluetoothLeAdapter adapter)
            throws BluetoothBeaconAdvertiserNotAvailable;

    /**
     * Delete the given scanner.
     * 
     * @param scanner
     *            The scanenr to be deleted
     */
    public void deleteBeaconScanner(BluetoothLeBeaconScanner<T> scanner);

    /**
     * Delete the given advertiser.
     * 
     * @param advertiser
     *            The advertiser to be deleted
     */
    public void deleteBeaconAdvertiser(BluetoothLeBeaconAdvertiser<T> advertiser);

}
