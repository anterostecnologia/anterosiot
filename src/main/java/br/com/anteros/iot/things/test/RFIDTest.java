package br.com.anteros.iot.things.test;

import com.diozero.util.Hex;
import com.diozero.util.SleepUtil;

/**
 * Control an LED with a button. To run:
 * <ul>
 * <li>sysfs:<br>
 * {@code java -cp tinylog-1.2.jar:diozero-core-$DIOZERO_VERSION.jar:diozero-sampleapps-$DIOZERO_VERSION.jar com.diozero.sampleapps.sandpit.MFRC522Test 0 0 25}
 * <li>JDK Device I/O 1.0:<br>
 * {@code sudo java -cp tinylog-1.2.jar:diozero-core-$DIOZERO_VERSION.jar:diozero-sampleapps-$DIOZERO_VERSION.jar:diozero-provider-jdkdio10-$DIOZERO_VERSION.jar:dio-1.0.1-dev-linux-armv6hf.jar -Djava.library.path=. com.diozero.sampleapps.sandpit.MFRC522Test 0 0 25}
 * <li>JDK Device I/O 1.1:<br>
 * {@code sudo java -cp tinylog-1.2.jar:diozero-core-$DIOZERO_VERSION.jar:diozero-sampleapps-$DIOZERO_VERSION.jar:diozero-provider-jdkdio11-$DIOZERO_VERSION.jar:dio-1.1-dev-linux-armv6hf.jar -Djava.library.path=. com.diozero.sampleapps.sandpit.MFRC522Test 0 0 25}
 * <li>Pi4j:<br>
 * {@code sudo java -cp tinylog-1.2.jar:diozero-core-$DIOZERO_VERSION.jar:diozero-sampleapps-$DIOZERO_VERSION.jar:diozero-provider-pi4j-$DIOZERO_VERSION.jar:pi4j-core-1.1-SNAPSHOT.jar com.diozero.sampleapps.sandpit.MFRC522Test 0 0 25}
 * <li>wiringPi:<br>
 * {@code sudo java -cp tinylog-1.2.jar:diozero-core-$DIOZERO_VERSION.jar:diozero-sampleapps-$DIOZERO_VERSION.jar:diozero-provider-wiringpi-$DIOZERO_VERSION.jar:pi4j-core-1.1-SNAPSHOT.jar com.diozero.sampleapps.sandpit.MFRC522Test 0 0 25}
 * <li>pigpgioJ:<br>
 * {@code sudo java -cp tinylog-1.2.jar:diozero-core-$DIOZERO_VERSION.jar:diozero-sampleapps-$DIOZERO_VERSION.jar:diozero-provider-pigpio-$DIOZERO_VERSION.jar:pigpioj-java-1.0.1.jar com.diozero.sampleapps.sandpit.MFRC522Test 0 0 25}
 * </ul>
 */
public class RFIDTest {
	public static void main(String[] args) {

		if (args.length < 3) {
			System.out.println("Usage: {} <spi-controller> <chip-select> <rst-gpio>");
			System.exit(1);
		}
		int index = 0;
		int controller = Integer.parseInt(args[index++]);
		int chip_select = Integer.parseInt(args[index++]);
		int reset_pin = Integer.parseInt(args[index++]);

		waitForCard(controller, chip_select, reset_pin);
	}

	private static void waitForCard(int controller, int chipSelect, int resetPin) {
		try (AnterosMFRC522 mfrc522 = new AnterosMFRC522(controller, chipSelect, resetPin)) {
			mfrc522.setLogReadsAndWrites(false);
//			if (mfrc522.performSelfTest()) {
//				System.out.println("Self test passed");
//			} else {
//				System.out.println("Self test failed");
//			}
			// Wait for a card
			AnterosMFRC522.UID uid = null;
			System.out.println("Waiting for a card");
			int i = 0;
			while (true) {
				
				uid = getID(mfrc522);
				if (uid != null) {
					i++;
					System.out.println(i+"-> uid: " + uid);
					System.out.println("");
					System.out.println("");
					System.out.println("Waiting for a card");
				}
				SleepUtil.sleepMillis(500);
			}
		}
	}

	private static AnterosMFRC522.UID getID(AnterosMFRC522 mfrc522) {
		// If a new PICC placed to RFID reader continue
		if (!mfrc522.isNewCardPresent()) {
			return null;
		}
		System.out.println("A card is present!");
		// Since a PICC placed get Serial and continue
		AnterosMFRC522.UID uid = mfrc522.readCardSerial();
		if (uid == null) {
			return null;
		}

		// There are Mifare PICCs which have 4 byte or 7 byte UID care if you use 7 byte
		// PICC
		// I think we should assume every PICC as they have 4 byte UID
		// Until we support 7 byte PICCs
		System.out.println(("Scanned PICC's UID: " + Hex.encodeHexString(uid.getUidBytes())));

		mfrc522.haltA();

		return uid;
	}
}