package br.com.anteros.iot.app;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

public class Wiegand {

        public static char[] s = new char[26];
        static int bits = 0;

        public static void main(String[] args) {
            System.setProperty("pi4j.linking", "dynamic");

            // create gpio controller
            final GpioController gpio = GpioFactory.getInstance();

            // provision gpio pin #02 as an input pin with its internal pull down
            // resistor enabled
            final GpioPinDigitalInput pin0 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_UP);
            final GpioPinDigitalInput pin1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_UP);

            System.out.println("PINs ready");
            Thread th = new Thread(new Runnable() {
                public void run() {

                    while (true) {

                        if (pin0.isLow()) { // D0 on ground?
                            s[bits++] = '0';
                            while (pin0.isLow()) { }
                            System.out.println(bits + "  " + 0);
                        }

                        if (pin1.isLow()) { // D1 on ground?
                            s[bits++] = '1';
                            while (pin1.isLow()) { }
                            System.out.println(bits + "  " + 1);
                        }

                        if (bits == 26) {
                            bits=0;
                            Print();
                        }

                    }

                }
            });
            th.setPriority(Thread.MAX_PRIORITY);
            th.start();
            System.out.println("Thread start");

            for (;;) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        protected static void Print() {

            String sonuc = "";
            for (int i = 0; i < 26; i++) {
                sonuc = sonuc+s[i];
                // System.out.write(s[i]);
            }
            int decimal = Integer.parseInt(sonuc,2);
            String hexStr = Integer.toString(decimal,16);
            System.out.println("Binary: " +sonuc);
            System.out.println("Hex: "+hexStr);
            System.out.println("Decimal: "+hex2decimal(hexStr));

            String facilityString = sonuc.substring(1,8);
            int facilityDecimal = Integer.parseInt(facilityString,2);
            String hexStrFacility = Integer.toString(facilityDecimal,16);
            System.out.println("Facility Code: " + hexStrFacility);
            System.out.println("Facility Code Decimal: " +hex2decimal(hexStrFacility));

            String cardNumber = sonuc.substring(9,25);
            int cardNumberDecimal = Integer.parseInt(cardNumber,2);
            String hexStringCardNumber = Integer.toString(cardNumberDecimal,16);
            System.out.println("Card Number: " +hexStringCardNumber);
            System.out.println("Card Number Decimal: " + hex2decimal(hexStringCardNumber));


            System.out.println();
            bits = 0;
        }

    public static int hex2decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }
}
