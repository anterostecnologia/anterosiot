package br.com.anteros.iot.app;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;

public class Wiegand {
	
		private static final Logger LOG = LoggerProvider.getInstance().getLogger(Wiegand.class.getName());

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

            LOG.info("PINs ready");
            Thread th = new Thread(new Runnable() {
                public void run() {

                    while (true) {

                        if (pin0.isLow()) { // D0 on ground?
                            s[bits++] = '0';
                            while (pin0.isLow()) { }
                            LOG.info(bits + "  " + 0);
                        }

                        if (pin1.isLow()) { // D1 on ground?
                            s[bits++] = '1';
                            while (pin1.isLow()) { }
                            LOG.info(bits + "  " + 1);
                        }

                        if (bits == 26) {
                            bits=0;
                            Print();
                        }

                    }

                }
            });
            th.setPriority(Thread.MAX_PRIORITY);
            th.setName("Wiegand");
            th.start();
            LOG.info("Thread start");

            for (;;) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        protected static void Print() {

            String sonuc = "";
            for (int i = 0; i < 26; i++) {
                sonuc = sonuc+s[i];
            }
            int decimal = Integer.parseInt(sonuc,2);
            String hexStr = Integer.toString(decimal,16);
            LOG.info("Binary: " +sonuc);
            LOG.info("Hex: "+hexStr);
            LOG.info("Decimal: "+hex2decimal(hexStr));

            String facilityString = sonuc.substring(1,8);
            int facilityDecimal = Integer.parseInt(facilityString,2);
            String hexStrFacility = Integer.toString(facilityDecimal,16);
            LOG.info("Facility Code: " + hexStrFacility);
            LOG.info("Facility Code Decimal: " +hex2decimal(hexStrFacility));

            String cardNumber = sonuc.substring(9,25);
            int cardNumberDecimal = Integer.parseInt(cardNumber,2);
            String hexStringCardNumber = Integer.toString(cardNumberDecimal,16);
            LOG.info("Card Number: " +hexStringCardNumber);
            LOG.info("Card Number Decimal: " + hex2decimal(hexStringCardNumber));


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
