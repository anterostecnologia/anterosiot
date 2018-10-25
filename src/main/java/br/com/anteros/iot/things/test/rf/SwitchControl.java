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

public class SwitchControl
{

    /**
     * Switch a remote switch on (Type D REV)
     *
     * @param sGroup        Code of the switch group (A,B,C,D)
     * @param nDevice       Number of the switch itself (1..3)
     */
    public void switchOn(char sGroup, int nDevice)
    {
        CodeWords.sendTriState( CodeWords.getCodeWordD(sGroup, nDevice, true) );
    }

    /**
     * Switch a remote switch off (Type D REV)
     *
     * @param sGroup        Code of the switch group (A,B,C,D)
     * @param nDevice       Number of the switch itself (1..3)
     */
    public void switchOff(char sGroup, int nDevice)
    {
        CodeWords.sendTriState( CodeWords.getCodeWordD(sGroup, nDevice, false) );
    }

    /**
     * Switch a remote switch on (Type C Intertechno)
     *
     * @param sFamily  Family code (a..f)
     * @param nGroup   Number of group (1..4)
     * @param nDevice  Number of device (1..4)
     */
    public void switchOn(char sFamily, int nGroup, int nDevice)
    {
        CodeWords.sendTriState( CodeWords.getCodeWordC(sFamily, nGroup, nDevice, true) );
    }

    /**
     * Switch a remote switch off (Type C Intertechno)
     *
     * @param sFamily  Family code (a..f)
     * @param nGroup   Number of group (1..4)
     * @param nDevice  Number of device (1..4)
     */
    public void switchOff(char sFamily, int nGroup, int nDevice)
    {
        CodeWords.sendTriState( CodeWords.getCodeWordC(sFamily, nGroup, nDevice, false) );
    }

    /**
     * Switch a remote switch on (Type B with two rotary/sliding switches)
     *
     * @param nAddressCode  Number of the switch group (1..4)
     * @param nChannelCode  Number of the switch itself (1..4)
     */
    public void switchOn(int nAddressCode, int nChannelCode)
    {
        CodeWords.sendTriState( CodeWords.getCodeWordB(nAddressCode, nChannelCode, true) );
    }

    /**
     * Switch a remote switch off (Type B with two rotary/sliding switches)
     *
     * @param nAddressCode  Number of the switch group (1..4)
     * @param nChannelCode  Number of the switch itself (1..4)
     */
    public void switchOff(int nAddressCode, int nChannelCode)
    {
        CodeWords.sendTriState( CodeWords.getCodeWordB(nAddressCode, nChannelCode, false) );
    }

    /**
     * Deprecated, use switchOn(final char* sGroup, final char* sDevice) instead!
     * Switch a remote switch on (Type A with 10 pole DIP switches)
     *
     * @param sGroup        Code of the switch group (refers to DIP switches 1..5 where "1" = on and "0" = off, if all DIP switches are on it's "11111")
     * @param nChannel      Number of the switch itself (1..5)
     */
    public void switchOn(final String sGroup, int nChannel)
    {
        final /*char* */ String[] code = { "00000", "10000", "01000", "00100", "00010", "00001" };
        this.switchOn(sGroup, code[nChannel]);
    }

    /**
     * Deprecated, use switchOff(final char* sGroup, final char* sDevice) instead!
     * Switch a remote switch off (Type A with 10 pole DIP switches)
     *
     * @param sGroup        Code of the switch group (refers to DIP switches 1..5 where "1" = on and "0" = off, if all DIP switches are on it's "11111")
     * @param nChannel      Number of the switch itself (1..5)
     */
    public void switchOff(final String sGroup, int nChannel)
    {
        final /*char* */ String[]code = { "00000", "10000", "01000", "00100", "00010", "00001" };
        this.switchOff(sGroup, code[nChannel]);
    }

    /**
     * Switch a remote switch on (Type A with 10 pole DIP switches)
     *
     * @param sGroup        Code of the switch group (refers to DIP switches 1..5 where "1" = on and "0" = off, if all DIP switches are on it's "11111")
     * @param sDevice       Code of the switch device (refers to DIP switches 6..10 (A..E) where "1" = on and "0" = off, if all DIP switches are on it's "11111")
     */
    public void switchOn(final String sGroup, final String sDevice)
    {
        CodeWords.sendTriState( CodeWords.getCodeWordA(sGroup, sDevice, true) );
    }

    /**
     * Switch a remote switch off (Type A with 10 pole DIP switches)
     *
     * @param sGroup        Code of the switch group (refers to DIP switches 1..5 where "1" = on and "0" = off, if all DIP switches are on it's "11111")
     * @param sDevice       Code of the switch device (refers to DIP switches 6..10 (A..E) where "1" = on and "0" = off, if all DIP switches are on it's "11111")
     */
    public void switchOff(final String sGroup, final String sDevice)
    {
        CodeWords.sendTriState( CodeWords.getCodeWordA(sGroup, sDevice, false) );
    }
}
