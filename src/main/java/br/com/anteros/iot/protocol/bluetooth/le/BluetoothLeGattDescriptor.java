/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package br.com.anteros.iot.protocol.bluetooth.le;

import java.util.UUID;

import br.com.anteros.iot.protocol.ble.exceptions.BluetoothIOException;

/**
 * BluetoothLeGattDescriptor represents a GATT descriptor.
 *
 * @noimplement This interface is not intended to be implemented by clients.
 * @since 1.3
 */

public interface BluetoothLeGattDescriptor {

    /**
     * Reads the value of this descriptor
     * 
     * @return A byte[] containing data from this descriptor
     * @throws BluetoothIOException
     */
    public byte[] readValue() throws BluetoothIOException;

    /**
     * Writes the value of this descriptor.
     * 
     * @param value
     *            The data as byte[] to be written
     * @throws BluetoothIOException
     */
    public void writeValue(byte[] value) throws BluetoothIOException;

    /**
     * Get the UUID of this descriptor.
     * 
     * @return The 128 byte UUID of this descriptor, NULL if an error occurred
     */
    public UUID getUUID();

    /**
     * Returns the characteristic to which this descriptor belongs to.
     * 
     * @return The characteristic.
     */
    public BluetoothLeGattCharacteristic getCharacteristic();

    /**
     * Returns the cached value of this descriptor, if any.
     * 
     * @return The cached value of this descriptor.
     */
    public byte[] getValue();

}
