package br.com.anteros.iot.support.sensor;

import com.pi4j.io.gpio.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SHT15 {

    private final GpioController gpio;
    private GpioPinDigitalOutput sck;
    private GpioPinDigitalMultipurpose data;


    private static final double D1 = -40.0;  //# for 14 Bit @ 5V
    private static final double D2 = 0.01; // # for 14 Bit DEGC

    private static final double C1 = -2.0468; //       # for 12 Bit
    private static final double C2 = 0.0367; //       # for 12 Bit
    private static final double C3 = -0.0000015955; // # for 12 Bit
    private static final double T1 = 0.01; //      # for 14 Bit @ 5V
    private static final double T2 = 0.00008; //   # for 14 Bit @ 5V

    private long lastInvocationTime;

    public static short[] bitStringToShortArray(String s) {
        int len = s.length();
        short[] data = new short[len];
        for (int i = 0; i < len; i++) {
            switch (s.charAt(i)) {
                case '0':
                    data[i] = 0;
                    break;
                case '1':
                    data[i] = 1;
                    break;
                default:
                    throw new IllegalArgumentException("String is allowed 0 1 only");

            }
        }
        return data;
    }

    public SHT15() {
        gpio = GpioFactory.getInstance();
        init();
    }

    public void reset() {
        data.high();
        for (int i = 9; i > 0; i--) {
            try {
                clockTick(PinState.HIGH);
                clockTick(PinState.LOW);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void clockTick(PinState state) throws InterruptedException {
        sck.setState(state);
        // Thread.sleep(0.);
        TimeUnit.NANOSECONDS.sleep(100);

    }

    private void init() {
        sck = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "SHT15 SCK", PinState.LOW);
        data = gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_00, "SHT DATA", PinMode.DIGITAL_OUTPUT);
        data.low();
    }


    public double readTemperature() throws InterruptedException {
        internalWait();
        return internalReadTemperature();
    }

    public double readHumidity() throws InterruptedException {
        internalWait();
        return internalReadHumidity(internalReadTemperature());

    }

    private double internalReadHumidity(double temperature) {
        try {
            short[] b = bitStringToShortArray("00000101");
            sendCommand(b);
            waitForResult();
            double rawHumidity = getData16Bit();
            skipCrc();

            double linearHumidity = C1 + C2 * rawHumidity + C3 * rawHumidity * rawHumidity;

            return (temperature - 25.0) * (T1 + T2 * rawHumidity) + linearHumidity;

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return Double.NaN;
        }
    }


    private double internalReadTemperature() {
        try {
            short[] b = bitStringToShortArray("00000011");
            sendCommand(b);
            waitForResult();
            double rawTemperature = getData16Bit();
            skipCrc();
            return rawTemperature * D2 + D1;
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return Double.NaN;
        }
    }

    private void skipCrc() throws InterruptedException {
        // Skip ack to end trans (no CRC)
        data.setMode(PinMode.DIGITAL_OUTPUT);
        data.high();
        clockTick(PinState.HIGH);
        clockTick(PinState.LOW);
    }

    private int getData16Bit() throws InterruptedException {
        data.setMode(PinMode.DIGITAL_INPUT);

        // Get the most significant bits
        int value = shiftIn(8);
        value *= 256;

        // Send the required ack
        data.setMode(PinMode.DIGITAL_OUTPUT);
        data.setState(PinState.HIGH);
        data.setState(PinState.LOW);
        clockTick(PinState.HIGH);
        clockTick(PinState.LOW);

        // Get the most significant bits
        data.setMode(PinMode.DIGITAL_INPUT);
        value |= shiftIn(8);

        return value;
    }

    private int shiftIn(int bitNum) throws InterruptedException {
        int value = 0;
        for (int i = 0; i < bitNum; i++) {
            clockTick(PinState.HIGH);
            value = value * 2 + data.getState().getValue();
            clockTick(PinState.LOW);
        }
        return value;
    }

    private void waitForResult() throws InterruptedException {
        data.setMode(PinMode.DIGITAL_INPUT);
        PinState ack = null;
        for (int i = 0; i < 100; i++) {
            TimeUnit.MILLISECONDS.sleep(10);
            ack = data.getState();
            if (ack == PinState.LOW) break;
        }
        if (ack == PinState.HIGH) throw new IllegalStateException("System Error");
    }

    private void sendCommand(short[] b) throws InterruptedException {

        data.setMode(PinMode.DIGITAL_OUTPUT);

        data.high();
        clockTick(PinState.HIGH);
        data.low();
        clockTick(PinState.LOW);
        clockTick(PinState.HIGH);
        data.high();
        clockTick(PinState.LOW);

        for (int i = 0; i < 8; i++) {
            data.setState(b[i] == 1 ? true : false);
            clockTick(PinState.HIGH);
            clockTick(PinState.LOW);
        }

        clockTick(PinState.HIGH);

        data.setMode(PinMode.DIGITAL_INPUT);
        PinState ack = data.getState();
        if (ack != PinState.LOW) {
            // Error
        }

        clockTick(PinState.LOW);

        ack = data.getState();

        if (ack != PinState.HIGH) {
            // Error
        }

    }

    private void internalWait() throws InterruptedException {
        long lastInvocationDelta = new Date().getTime() - lastInvocationTime;
        if (lastInvocationDelta < 1000) {
            TimeUnit.MILLISECONDS.sleep(lastInvocationDelta);
        }
        lastInvocationTime = new Date().getTime();
    }


}
