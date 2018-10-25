package br.com.anteros.iot.things.test.rf;



class CodeWord
{
    private final TriState[] word;
    private boolean valid;
    private int triBitCount;

    CodeWord() 
    {
        word = new TriState[12];
        valid=false;
        triBitCount=0; 
    }
    //TriState getTriStateBit(int i){return word[i];}
    //void setTriStateBit(int i,TriState t){word[i] = t;}
    void setValid(boolean v){valid = v;}
    void addTriStateBit(TriState t)
    {
        word[this.triBitCount] = t;
        triBitCount++;
    }
    boolean isValid(){return valid;}
    TriState[] getWord(){return word;}
}

enum TriState {zero,one,floating}

/**
 * CodeWords    -   This class constructs the codewords for different switch types
 *
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

public class CodeWords
{

    /**
     * @param cw   a tristate code word consisting of the letter 0, 1, F
     */
    public static void sendTriState(CodeWord cw)
    {
        if(!cw.isValid()) return;
        // turn the tristate code word into the corresponding bit pattern, then send it
        /*unsigned*/ long code = 0;
        /*unsigned*/ int length = 0;
        for (TriState t:cw.getWord())
        {
            code <<= 2L;
            switch (t) {
                case zero:                  // bit pattern 00, no action needed
                    break;
                case floating: code |= 1L;  // bit pattern 01
                    break;
                case one: code |= 3L;       // bit pattern 11
                    break;
            }
            length += 2;
        }
        Main.getTransmitter().send(code, length);
    }


    /**
     * Encoding for type A switches with two rotary/sliding switches.
     *
     * The code word is a tristate word and with following bit pattern:
     *
     * +-----------------------------+-----------------------------+----------+------------+
     * | 5 bits address              | 5 bits address              | 1bit(inv)| 1 bit      |
     * | switch group                | switch number               | on / off | on / off   |
     * | 1=0FFF 2=F0FF 3=FF0F 4=FFF0 | 1=0FFF 2=F0FF 3=FF0F 4=FFF0 |on=0 off=F| on=F off=0 |
     * +-----------------------------+-----------------------------+----------+------------+
     *
     * @param sGroup    Number of the switch group (binary string )
     * @param sDevice   Number of the switch itself (binary string)
     * @param bStatus   Whether to switch on (true) or off (false)
     *
     * @return char[13], representing a tristate code word of length 12
     */
    public static CodeWord getCodeWordA(String sGroup, String sDevice, boolean bStatus)
    {
        CodeWord cw = new CodeWord();
        cw.setValid(false);

        for (int i = 0; i < 5; i++) {
            cw.addTriStateBit((sGroup.charAt(i) == '0') ? TriState.floating : TriState.zero);
        }
        for (int i = 0; i < 5; i++) {
            cw.addTriStateBit((sDevice.charAt(i) == '0') ? TriState.floating : TriState.zero);
        }
        cw.addTriStateBit(bStatus ? TriState.zero : TriState.floating);
        cw.addTriStateBit(bStatus ? TriState.floating : TriState.zero);
        cw.setValid(true);
        return cw;
    }

    /**
     * Encoding for type B switches with two rotary/sliding switches.
     *
     * The code word is a tristate word and with following bit pattern:
     *
     * +-----------------------------+-----------------------------+----------+------------+
     * | 4 bits address              | 4 bits address              | 3 bits   | 1 bit      |
     * | switch group                | switch number               | not used | on / off   |
     * | 1=0FFF 2=F0FF 3=FF0F 4=FFF0 | 1=0FFF 2=F0FF 3=FF0F 4=FFF0 | FFF      | on=F off=0 |
     * +-----------------------------+-----------------------------+----------+------------+
     *
     * @param nAddressCode  Number of the switch group (1..4)
     * @param nChannelCode  Number of the switch itself (1..4)
     * @param bStatus       Whether to switch on (true) or off (false)
     *
     * @return char[13], representing a tristate code word of length 12
     */
    public static CodeWord  getCodeWordB(int nAddressCode, int nChannelCode, boolean bStatus)
    {
        CodeWord cw = new CodeWord();
        cw.setValid(false);

        if (nAddressCode < 1 || nAddressCode > 4 || nChannelCode < 1 || nChannelCode > 4) 
        {
            return cw;
        }

        for (int i = 1; i <= 4; i++) {
            cw.addTriStateBit((nAddressCode == i) ? TriState.zero : TriState.floating);
        }

        for (int i = 1; i <= 4; i++) {
            cw.addTriStateBit((nChannelCode == i) ? TriState.zero : TriState.floating);
        }

        cw.addTriStateBit(TriState.floating);
        cw.addTriStateBit(TriState.floating);
        cw.addTriStateBit(TriState.floating);

        cw.addTriStateBit(bStatus ? TriState.floating : TriState.zero);

        cw.setValid(true);
        return cw;
    }

    /**
     * Like getCodeWord (Type C = Intertechno)
     * @param sFamily   String containing the switch family 'a'-'o'
     * @param nGroup    the number of the switch group 1-4
     * @param nDevice   the switch number 1-4
     * @param bStatus   true = on false = off
     * @return          The CodeWord command for the device
     */
    public static CodeWord getCodeWordC(char sFamily, int nGroup, int nDevice, boolean bStatus)
    {
        CodeWord cw = new CodeWord();
        cw.setValid(false);
        int nFamily = (int)sFamily - 'a';
        if ( nFamily < 0 || nFamily > 15 || nGroup < 1 || nGroup > 4 || nDevice < 1 || nDevice > 4) {
            return null ;//0
        }

        // encode the family into four bits
        cw.addTriStateBit(((nFamily & 1 )>0) ? TriState.floating : TriState.zero);
        cw.addTriStateBit(((nFamily & 2))>0 ? TriState.floating : TriState.zero);
        cw.addTriStateBit(((nFamily & 4))>0 ? TriState.floating : TriState.zero);
        cw.addTriStateBit(((nFamily & 8))>0 ? TriState.floating : TriState.zero);

        // encode the device and group
        cw.addTriStateBit((((nDevice-1) & 1))>0 ? TriState.floating : TriState.zero);
        cw.addTriStateBit((((nDevice-1) & 2))>0 ? TriState.floating : TriState.zero);
        cw.addTriStateBit((((nGroup-1) & 1))>0 ? TriState.floating : TriState.zero);
        cw.addTriStateBit((((nGroup-1) & 2))>0 ? TriState.floating : TriState.zero);

        // encode the status code
        cw.addTriStateBit(TriState.zero);
        cw.addTriStateBit(TriState.floating);
        cw.addTriStateBit(TriState.floating);
        cw.addTriStateBit(bStatus ? TriState.floating : TriState.zero);

        cw.setValid(true);
        return cw;
    }

    /**
     * Encoding for the REV Switch Type
     *
     * The code word is a tristate word and with following bit pattern:
     *
     * +-----------------------------+-------------------+----------+--------------+
     * | 4 bits address              | 3 bits address    | 3 bits   | 2 bits       |
     * | switch group                | device number     | not used | on / off     |
     * | A=1FFF B=F1FF C=FF1F D=FFF1 | 1=0FF 2=F0F 3=FF0 | 000      | on=10 off=01 |
     * +-----------------------------+-------------------+----------+--------------+
     *
     * Source: http://www.the-intruder.net/funksteckdosen-von-rev-uber-arduino-ansteuern/
     *
     * @param sGroup        Name of the switch group (A..D, resp. a..d)
     * @param nDevice       Number of the switch itself (1..3)
     * @param bStatus       Whether to switch on (true) or off (false)
     *
     * @return char[13], representing a tristate code word of length 12
     */
    public static CodeWord getCodeWordD(char sGroup, int nDevice, boolean bStatus)
    {
        CodeWord cw = new CodeWord();
        cw.setValid(false);

        // sGroup must be one of the letters in "abcdABCD"
        int nGroup = (sGroup >= 'a') ? (int)sGroup - 'a' : (int)sGroup - 'A';
        if ( nGroup < 0 || nGroup > 3 || nDevice < 1 || nDevice > 3) {
            return cw;
        }

        for (int i = 0; i < 4; i++) {
            cw.addTriStateBit((nGroup == i) ? TriState.one : TriState.floating);
        }

        for (int i = 1; i <= 3; i++) {
            cw.addTriStateBit((nDevice == i) ? TriState.one : TriState.floating);
        }

        cw.addTriStateBit(TriState.zero);
        cw.addTriStateBit(TriState.zero);
        cw.addTriStateBit(TriState.zero);

        cw.addTriStateBit(bStatus ? TriState.one : TriState.zero);
        cw.addTriStateBit(bStatus ? TriState.zero : TriState.one);

        cw.setValid(true);
        return cw;
    }
}