/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package br.com.anteros.iot.protocol.ble.beacon;


import br.com.anteros.iot.protocol.ble.exceptions.BLECommandException;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeAdapter;
import br.com.anteros.iot.protocol.bluetooth.le.beacon.BluetoothLeBeacon;
import br.com.anteros.iot.protocol.bluetooth.le.beacon.BluetoothLeBeaconAdvertiser;
import br.com.anteros.iot.protocol.bluetooth.le.beacon.BluetoothLeBeaconEncoder;

public class BluetoothLeBeaconAdvertiserImpl<T extends BluetoothLeBeacon> implements BluetoothLeBeaconAdvertiser<T> {

    private final BluetoothLeAdapter adapter;
    private final BluetoothLeBeaconEncoder<T> encoder;
    private final BluetoothLeBeaconManagerImpl beaconManager;

    public BluetoothLeBeaconAdvertiserImpl(BluetoothLeAdapter adapter, BluetoothLeBeaconEncoder<T> encoder,
            BluetoothLeBeaconManagerImpl beaconManager) {
        this.adapter = adapter;
        this.encoder = encoder;
        this.beaconManager = beaconManager;
    }

    @Override
    public BluetoothLeAdapter getAdapter() {
        return this.adapter;
    }

    @Override
    public void startBeaconAdvertising() throws BLECommandException {
        this.beaconManager.startBeaconAdvertising(this.adapter.getInterfaceName());
    }

    @Override
    public void stopBeaconAdvertising() throws BLECommandException {
        this.beaconManager.stopBeaconAdvertising(this.adapter.getInterfaceName());
    }

    @Override
    public void updateBeaconAdvertisingInterval(Integer min, Integer max) throws BLECommandException {
        this.beaconManager.updateBeaconAdvertisingInterval(min, max, this.adapter.getInterfaceName());
    }

    @Override
    public void updateBeaconAdvertisingData(T beacon) throws BLECommandException {
        this.beaconManager.updateBeaconAdvertisingData((BluetoothLeBeacon) beacon,
                (BluetoothLeBeaconEncoder<BluetoothLeBeacon>) this.encoder, this.adapter.getInterfaceName());
    }
}
