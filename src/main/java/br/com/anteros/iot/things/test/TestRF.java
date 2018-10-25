package br.com.anteros.iot.things.test;

import java.util.BitSet;

import com.pi4j.io.gpio.RaspiPin;

public class TestRF {

	public static void main(String[] args) {		

		//our switching group address is 01011 (marked with 1 to 5 on the DIP switch
		//on the switching unit itself)
		BitSet address = RCSwitch.getSwitchGroupAddress("010101");

		RCSwitch transmitter = new RCSwitch(RaspiPin.GPIO_00);
		transmitter.switchOn(address, 1); //switches the switch unit A (A = 1, B = 2, ...) on
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //wait 5 sec.
		transmitter.switchOff(address, 1); //switches the switch unit A off
	}

}
