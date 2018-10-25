package br.com.anteros.iot.things.test.rf;

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

/* Format for protocol definitions:
 * {pulseLength, Sync bit, "0" bit, "1" bit}
 *
 * pulseLength: pulse length in microseconds, e.g. 350
 * Sync bit: {1, 31} means 1 high pulse and 31 low pulses
 *     (perceived as a 31*pulseLength long pulse, total length of sync bit is
 *     32*pulseLength microseconds), i.e:
 *      _
 *     | |_______________________________ (don't count the vertical bars)
 * "0" bit: waveform for a data bit of value "0", {1, 3} means 1 high pulse
 *     and 3 low pulses, total length (1+3)*pulseLength, i.e:
 *      _
 *     | |___
 * "1" bit: waveform for a data bit of value "1", e.g. {3,1}:
 *      ___
 *     |   |_
 *
 * These are combined to form Tri-State bits when sending or receiving codes.
 *    { 350, {  1, 31 }, {  1,  3 }, {  3,  1 }, false },    // protocol 1
 *    { 650, {  1, 10 }, {  1,  2 }, {  2,  1 }, false },    // protocol 2
 *    { 100, { 30, 71 }, {  4, 11 }, {  9,  6 }, false },    // protocol 3
 *    { 380, {  1,  6 }, {  1,  3 }, {  3,  1 }, false },    // protocol 4
 *    { 500, {  6, 14 }, {  1,  2 }, {  2,  1 }, false },    // protocol 5
 *    { 450, { 23,  1 }, {  1,  2 }, {  2,  1 }, true }      // protocol 6 (HT6P20B)
 */
public enum Protocol
{
    protocol1 (1,350,1,31,1,3,3,1,false ),
    protocol2 (2,650,1,10,1,2,2,1,false),
    protocol3 (3,100,30,71,4,11,9,6,false ),
    protocol4 (4,380,1,6,1,3,3,1,false ),
    protocol5 (5,500,6,14,1,2,2,1,false ),
    protocol6 (6,450,23,1,1,2,2,1,true ), // (HT6P20B)
    protocol7 (7,590,36,1,1,2,2,1,true); // garage door controller

    public final int protocolNumber;
    public final int pulseLength;
    public final HighLow syncFactor;
    public final HighLow zero;
    public final HighLow one;
    public final boolean invertedSignal; //if true inverts the high and low logic levels in the HighLow structs

    Protocol(int n,int l, int sfh, int sfl, int zh, int zl, int oh, int ol, boolean inv)
    {
        this.protocolNumber = n;
        this.pulseLength = l;
        this.syncFactor = new HighLow((byte)sfh, (byte)sfl);
        this.zero = new HighLow((byte)zh, (byte) zl);
        this.one = new HighLow((byte)oh, (byte) ol);
        this.invertedSignal = inv;
    }

}
