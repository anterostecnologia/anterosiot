/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package br.com.anteros.iot.protocol.ble.util;

import br.com.anteros.iot.protocol.ble.exceptions.BLEException;

public interface BluetoothProcessListener {

    public void processInputStream(String string) throws BLEException;

    public void processInputStream(int ch) throws BLEException;

    public void processErrorStream(String string) throws BLEException;

}
