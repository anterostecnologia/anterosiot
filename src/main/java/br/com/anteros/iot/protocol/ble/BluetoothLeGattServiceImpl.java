/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package br.com.anteros.iot.protocol.ble;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.anteros.iot.protocol.ble.exceptions.BluetoothResourceNotFoundException;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeDevice;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeGattCharacteristic;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeGattService;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattService;

public class BluetoothLeGattServiceImpl implements BluetoothLeGattService {

    private static final long TIMEOUT = 30;

    private final BluetoothGattService service;

    public BluetoothLeGattServiceImpl(BluetoothGattService service) {
        this.service = service;
    }

    @Override
    public BluetoothLeGattCharacteristic findCharacteristic(UUID uuid) throws BluetoothResourceNotFoundException {
        return findCharacteristic(uuid, BluetoothLeGattServiceImpl.TIMEOUT);
    }

    @Override
    public BluetoothLeGattCharacteristic findCharacteristic(UUID uuid, long timeout)
            throws BluetoothResourceNotFoundException {
        BluetoothGattCharacteristic characteristic;
        characteristic = this.service.find(uuid.toString(), Duration.ofSeconds(timeout));
        if (characteristic != null) {
            return new BluetoothLeGattCharacteristicImpl(characteristic);
        } else {
            throw new BluetoothResourceNotFoundException("Gatt characteristic not found");
        }
    }

    @Override
    public List<BluetoothLeGattCharacteristic> findCharacteristics() throws BluetoothResourceNotFoundException {
        List<BluetoothGattCharacteristic> tinybCharacteristics = this.service.getCharacteristics();
        List<BluetoothLeGattCharacteristic> characteristics = new ArrayList<>();
        if (tinybCharacteristics != null) {
            for (BluetoothGattCharacteristic characteristic : tinybCharacteristics) {
                characteristics.add(new BluetoothLeGattCharacteristicImpl(characteristic));
            }
        } else {
            throw new BluetoothResourceNotFoundException("Gatt characteristics not found");
        }
        return characteristics;
    }

    @Override
    public UUID getUUID() {
        return UUID.fromString(this.service.getUUID());
    }

    @Override
    public BluetoothLeDevice getDevice() {
        return new BluetoothLeDeviceImpl(this.service.getDevice());
    }

    @Override
    public boolean isPrimary() {
        return this.service.getPrimary();
    }

}
