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

import java.util.UUID;

import br.com.anteros.iot.protocol.ble.exceptions.BluetoothIOException;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeGattCharacteristic;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeGattDescriptor;
import tinyb.BluetoothException;
import tinyb.BluetoothGattDescriptor;

public class BluetoothLeGattDescriptorImpl implements BluetoothLeGattDescriptor {

    private final BluetoothGattDescriptor descriptor;

    public BluetoothLeGattDescriptorImpl(BluetoothGattDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public byte[] readValue() throws BluetoothIOException {
        byte[] value;
        try {
            value = BluetoothLeGattDescriptorImpl.this.descriptor.readValue();
        } catch (BluetoothException e) {
            throw new BluetoothIOException(e, "Read descriptor value failed");
        }
        return value;
    }

    @Override
    public void writeValue(byte[] value) throws BluetoothIOException {
        try {
            BluetoothLeGattDescriptorImpl.this.descriptor.writeValue(value);
        } catch (BluetoothException e) {
            throw new BluetoothIOException(e, "Write descriptor value failed");
        }
    }

    @Override
    public UUID getUUID() {
        return UUID.fromString(this.descriptor.getUUID());
    }

    @Override
    public BluetoothLeGattCharacteristic getCharacteristic() {
        return new BluetoothLeGattCharacteristicImpl(this.descriptor.getCharacteristic());
    }

    @Override
    public byte[] getValue() {
        return this.descriptor.getValue();
    }

}
