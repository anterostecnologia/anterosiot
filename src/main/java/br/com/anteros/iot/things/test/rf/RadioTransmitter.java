package br.com.anteros.iot.things.test.rf;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;


/**
 * ported to java for Raspberry Pi by GJWood on 14/01/2017.
 *
 * RCSwitch - Arduino library for remote control outlet switches
 * Copyright (c) 2011 Suat Özgür.  All right reserved.
 *
 * Contributors:
 * - Andre Koehler / info(at)tomate-online(dot)de
 * - Gordeev Andrey Vladimirovich / gordeev(at)openpyro(dot)com
 * - Skineffect / http://forum.ardumote.com/viewtopic.php?f=2&t=46
 * - Dominik Fischer / dom_fischer(at)web(dot)de
 * - Frank Oltmanns / <first name>.<last name>(at)gmail(dot)com
 * - Andreas Steinel / A.<lastname>(at)gmail(dot)com
 * - Max Horn / max(at)quendi(dot)de
 * - Robert ter Vehn / <first name>.<last name>(at)gmail(dot)com
 * - Johann Richard / <first name>.<last name>(at)gmail(dot)com
 * - Vlad Gheorghe / <first name>.<last name>(at)gmail(dot)com https://github.com/vgheo

 * Project home: https://github.com/sui77/rc-switch/
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, In
 */
public class RadioTransmitter
{
    private Protocol protocol;
    private int nRepeatTransmit;
    private int nTransmitterPin;
    final private GpioPinDigitalOutput transmitterPin;

    public RadioTransmitter(GpioPinDigitalOutput transmitterPin)
    {
        this.transmitterPin = transmitterPin;
        transmitterPin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        this.nTransmitterPin = Integer.parseInt(transmitterPin.getPin().getName().substring(5));
        this.setRepeatTransmit(10);
        this.setProtocol(1);
    }


    /**
     * setProtocol  -   Sets the protocol to send.
     * @param protocol  protocol type, see Protocol class
     */
    public void setProtocol(Protocol protocol)
    {
        this.protocol = protocol;
    }

    /**
     * setProtocol  -   Sets the protocol to send, from a list of predefined protocols
     * &param nProtocol the protocol number 1-6
     */
    public void setProtocol(int nProtocol)
    {
        for(Protocol p: Protocol.values())
        {
            if (p.protocolNumber == nProtocol) this.protocol = p;
            return;
        }
        // TODO: trigger an error, e.g. "bad protocol" ???
        this.protocol = Protocol.protocol6;
    }

    /**
     * Sets the protocol to send with pulse length in microseconds.
     */
    public void setProtocol(int nProtocol, int nPulseLength)
    {
        setProtocol(nProtocol);
        this.setPulseLength(nPulseLength);
    }


    /**
     * Sets pulse length in microseconds
     */
    public void setPulseLength(int nPulseLength)
    {
        // TODO this.protocol.pulseLength = nPulseLength;
    }

    /**
     * Sets Repeat Transmits
     */
    public void setRepeatTransmit(int nRepeatTransmit)
    {
        this.nRepeatTransmit = nRepeatTransmit;
    }

    /**
     * Enable transmissions
     *
     * @param nTransmitterPin    Arduino Pin to which the sender is connected to
     */
    public void enableTransmit(int nTransmitterPin)
    {
        // set up in constructor
        //this.nTransmitterPin = nTransmitterPin;
        //pinMode(this.nTransmitterPin, OUTPUT);
    }

    /**
     * Disable transmissions
     */
    public void disableTransmit()
    {
        this.nTransmitterPin = -1;
    }

    /**
     * @param sCodeWord   a binary code word consisting of the letter 0, 1
     */
    public void send(final String sCodeWord)
    {
        // turn the tristate code word into the corresponding bit pattern, then send it
        /*unsigned*/ long code = 0;
        /*unsigned*/ int length = 0;
        for (int i = 0; i< sCodeWord.length(); i++) {
            code <<= 1L;
            if (sCodeWord.charAt(i) != '0')
            code |= 1L;
            length++;
        }
        this.send(code, length);
    }

    /**
     * Transmit the first 'length' bits of the integer 'code'. The
     * bits are sent from MSB to LSB, i.e., first the bit at position length-1,
     * then the bit at position length-2, and so on, till finally the bit at position 0.
     */
    public void send(/*unsigned*/ long code, /*unsigned*/ int length)
    {
        if (this.nTransmitterPin == -1)
        return;

    //#if not defined( RCSwitchDisableReceiving )
        // make sure the receiver is disabled while we transmit
//        int nReceiverInterrupt_backup = Main.getReceiver().getnReceiverInterrupt();
//        if (nReceiverInterrupt_backup != -1) {
//            //Main.getReceiver().disableReceive();
//        }
    //#endif

        for (int nRepeat = 0; nRepeat < nRepeatTransmit; nRepeat++) {
            for (int i = length-1; i >= 0; i--) {
                if ((code & (1L << i))>0)
                    this.transmit(protocol.one);
          else
                this.transmit(protocol.zero);
            }
            this.transmit(protocol.syncFactor);
        }
        // enable receiver again if we just disabled it
//        if (nReceiverInterrupt_backup != -1) {
//            //Main.getReceiver().enableReceive(nReceiverInterrupt_backup);
//        }
    }

    /**
     * Transmit a single high-low pulse.
     */
    public void transmit(HighLow pulses)
    {
        PinState firstLogicLevel = (this.protocol.invertedSignal) ? PinState.LOW : PinState.HIGH;
        PinState secondLogicLevel = (this.protocol.invertedSignal) ? PinState.HIGH : PinState.LOW;

        this.transmitterPin.setState( firstLogicLevel);
        Gpio.delayMicroseconds( this.protocol.pulseLength * pulses.high);
        this.transmitterPin.setState(secondLogicLevel);
        Gpio.delayMicroseconds( this.protocol.pulseLength * pulses.low);
    }

//#endif
}
