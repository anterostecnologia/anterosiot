/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package br.com.anteros.iot.protocol.bluetooth.le.beacon.listener;

import br.com.anteros.iot.protocol.bluetooth.le.beacon.BluetoothLeBeacon;

/**
 * BluetoothLeBeaconListener must be implemented by any class wishing to receive BLE beacon data
 *
 * @since 1.3
 */

@FunctionalInterface
public interface BluetoothLeBeaconListener<T extends BluetoothLeBeacon> {

    /**
     * Fired when Bluetooth LE beacons data is received
     *
     * @param beacon
     *            a received beacon
     */
    public void onBeaconsReceived(T beacon);
}