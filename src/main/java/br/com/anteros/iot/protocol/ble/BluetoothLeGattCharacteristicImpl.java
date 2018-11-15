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
import java.util.function.Consumer;

import br.com.anteros.iot.protocol.ble.exceptions.BluetoothIOException;
import br.com.anteros.iot.protocol.ble.exceptions.BluetoothNotificationException;
import br.com.anteros.iot.protocol.ble.exceptions.BluetoothResourceNotFoundException;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeGattCharacteristic;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeGattCharacteristicProperties;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeGattDescriptor;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeGattService;
import tinyb.BluetoothException;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattDescriptor;

public class BluetoothLeGattCharacteristicImpl implements BluetoothLeGattCharacteristic {

    private static final long TIMEOUT = 30;

    private final BluetoothGattCharacteristic characteristic;

    public BluetoothLeGattCharacteristicImpl(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }

    @Override
    public BluetoothLeGattDescriptor findDescriptor(UUID uuid) throws BluetoothResourceNotFoundException {
        return findDescriptor(uuid, BluetoothLeGattCharacteristicImpl.TIMEOUT);
    }

    @Override
    public BluetoothLeGattDescriptor findDescriptor(UUID uuid, long timeout)
            throws BluetoothResourceNotFoundException {
        BluetoothGattDescriptor descriptor;
        descriptor = this.characteristic.find(uuid.toString(), Duration.ofSeconds(timeout));
        if (descriptor != null) {
            return new BluetoothLeGattDescriptorImpl(descriptor);
        } else {
            throw new BluetoothResourceNotFoundException("Descriptor not found");
        }
    }

    @Override
    public List<BluetoothLeGattDescriptor> findDescriptors() throws BluetoothResourceNotFoundException {
        List<BluetoothGattDescriptor> tinybDescriptors = this.characteristic.getDescriptors();
        List<BluetoothLeGattDescriptor> descriptors = new ArrayList<>();
        if (tinybDescriptors != null) {
            for (BluetoothGattDescriptor descriptor : tinybDescriptors) {
                descriptors.add(new BluetoothLeGattDescriptorImpl(descriptor));
            }
        } else {
            throw new BluetoothResourceNotFoundException("Descriptors not found");
        }
        return descriptors;
    }

    @Override
    public byte[] readValue() throws BluetoothIOException {
        byte[] value;
        try {
            value = BluetoothLeGattCharacteristicImpl.this.characteristic.readValue();
        } catch (BluetoothException e) {
            throw new BluetoothIOException(e, "Read characteristic value failed");
        }
        return value;
    }

    @Override
    public void enableValueNotifications(Consumer<byte[]> callback) throws BluetoothNotificationException {
        BluetoothLeNotification<byte[]> notification = new BluetoothLeNotification<>(callback);
        try {
            this.characteristic.enableValueNotifications(notification);
        } catch (Exception e) {
            throw new BluetoothNotificationException(e, "Notification can't be enabled");
        }
    }

    @Override
    public void disableValueNotifications() throws BluetoothNotificationException {
        try {
            this.characteristic.disableValueNotifications();
        } catch (Exception e) {
            throw new BluetoothNotificationException(e, "Notification can't be disabled");
        }
    }

    @Override
    public void writeValue(byte[] value) throws BluetoothIOException {
        try {
            BluetoothLeGattCharacteristicImpl.this.characteristic.writeValue(value);
        } catch (BluetoothException e) {
            throw new BluetoothIOException(e, "Write characteristic value failed");
        }
    }

    @Override
    public UUID getUUID() {
        return UUID.fromString(this.characteristic.getUUID());
    }

    @Override
    public BluetoothLeGattService getService() {
        return new BluetoothLeGattServiceImpl(this.characteristic.getService());
    }

    @Override
    public byte[] getValue() {
        return this.characteristic.getValue();
    }

    @Override
    public boolean isNotifying() {
        return this.characteristic.getNotifying();
    }

    @Override
    public List<BluetoothLeGattCharacteristicProperties> getProperties() {
        List<BluetoothLeGattCharacteristicProperties> properties = new ArrayList<>();
        for (String flag : this.characteristic.getFlags()) {
            properties.add(BluetoothLeGattCharacteristicProperties.valueOf(flag));
        }
        return properties;
    }

}
