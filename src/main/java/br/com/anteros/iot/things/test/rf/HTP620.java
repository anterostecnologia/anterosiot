package br.com.anteros.iot.things.test.rf;

import java.nio.ByteBuffer;

import com.diozero.util.SleepUtil;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.iot.support.Pi4JHelper;

public class HTP620 {

	private boolean startbit;
	private int counter;
	private long buffer, addressFull;
	private int dur0, dur1;
	private int lambda_TX = 350;
	private int pinRF_RX;
	private int pinRF_TX;
	private GpioController gpio;
	private GpioPinDigitalInput pinRX;
	private GpioPinDigitalOutput pinTX;

	public HTP620() {
		gpio = Pi4JHelper.getGpioController();
	}

	public void beginRX(int _pinRX) {
		pinRF_RX = _pinRX;
		pinRX = Pi4JHelper.getDigitalInputPin(gpio, _pinRX);
	}

	public void beginTX(int _pinTX) {
		pinRF_TX = _pinTX;
		pinTX = Pi4JHelper.getDigitalOutputPin(gpio, _pinTX, "RF_TX");
		gpio.low(pinTX);
	}

	public void beginRXTX(int _pinRX, int _pinTX) {
		beginRX(_pinRX);
		beginTX(_pinTX);
	}

	public long getCode() {
		return (addressFull);
	}

	boolean available() {

		int lambda_RX;
		if (!startbit) {// Check the PILOT CODE until START BIT;
			
			//AQUI dur0 = pulseIn(pinRF_RX, LOW); // Check how long DOUT was "0" (ZERO) (refers to PILOT CODE)

			// If time at "0" is between 9200 us (23 cycles of 400us) and 13800 us (23
			// cycles of 600 us).
			if ((dur0 > 9200) && (dur0 < 13800) && !startbit) {
				// calculate wave length - lambda
				lambda_RX = dur0 / 23;
				// Reset variables
				dur0 = 0;
				buffer = 0;
				counter = 0;
				startbit = true;
			}
		}

		// If Start Bit is OK, then starts measure os how long the signal is level "1"
		// and check is value is into acceptable range.
		if (startbit && counter < 28) {
			++counter;

//			//AQUI dur1 = pulseIn(pinRF_RX, HIGH);
//
//			if ((dur1 > 0.5 * lambda_RX) && (dur1 < (1.5 * lambda_RX))) // If pulse width at "1" is between "0.5 and 1.5
//																		// lambda", means that pulse is only one lambda,
//																		// so the data Ã© "1".
//			{
//				buffer = (buffer << 1) + 1; // add "1" on data buffer
//			} else if ((dur1 > 1.5 * lambda_RX) && (dur1 < (2.5 * lambda_RX))) // If pulse width at "1" is between "1.5
//																				// and 2.5 lambda", means that pulse is
//																				// two lambdas, so the data Ã© "0".
//			{
//				buffer = (buffer << 1); // add "0" on data buffer
//			} else {
//				// Reset the loop
//				startbit = false;
//			}
		}

		// Check if all 28 bits were received (22 of Address + 2 of Data + 4 of
		// Anti-Code)
		if (counter == 28) {
			// Check if Anti-Code is OK (last 4 bits of buffer equal "0101")
			if ((bitRead(buffer, 0) == 1) && (bitRead(buffer, 1) == 0) && (bitRead(buffer, 2) == 1)
					&& (bitRead(buffer, 3) == 0)) {
				counter = 0;
				startbit = false;

				// Get ADDRESS COMPLETO from Buffer
				addressFull = buffer;

				// If a valid data is received, return OK
				return true;
			} else {
				// Reset the loop
				startbit = false;
			}
		}

		// If none valid data is received, return NULL and FALSE values
		addressFull = 0;

		return false;
	}

	void sendData(char data) {
		int pulse = (int) lambda_TX;

		if (data == '0') {
			gpio.low(pinTX);
			myDelay(pulse);

			gpio.high(pinTX);
			myDelay(2 * pulse);
		}

		if (data == '1') {
			gpio.low(pinTX);
			myDelay(2 * pulse);

			gpio.high(pinTX);
			myDelay(pulse);
		}
	}

	void myDelay(int t) {
		SleepUtil.sleepMicros(t);
	}

	private int bitRead(long buffer, int i) {
		String binaryString = Long.toBinaryString(buffer);
		return new Integer(binaryString.toCharArray()[i]).intValue();
	}

	void sendPilotCode() {
		// Keep pinRF on HIGH for little time
		gpio.high(pinTX);
		SleepUtil.sleepMicros(500);

		// Set pinRF on LOW for 23 Lambdas
		gpio.low(pinTX);
		myDelay(23 * lambda_TX);

		// Set pinRF on HIGH for one Lambda
		gpio.high(pinTX);
		myDelay(lambda_TX);
	}

	void sendCode(long addressCodeHEX) {
		String addressCodeBIN = "0000000000000000000000000000" + Long.toBinaryString(addressCodeHEX);

		addressCodeBIN = addressCodeBIN.substring(addressCodeBIN.length() - 28, addressCodeBIN.length());

		if (!(addressCodeHEX == 0)) {
			// Send PILOTE CODE (details:
			// http://acturcato.wordpress.com/2014/01/04/decoder-for-ht6p20b-encoder-on-arduino-board-english/)
			sendPilotCode();

			// Send all bits for Address Code
			for (int i = 0; i < 28; i++) {
				char data = addressCodeBIN.charAt(i);
				sendData(data);
			}
		}
		// Disables transmissions
		gpio.low(pinTX);
	}

}
