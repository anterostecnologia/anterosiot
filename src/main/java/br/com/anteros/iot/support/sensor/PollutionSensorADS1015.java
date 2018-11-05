package br.com.anteros.iot.support.sensor;

import com.pi4j.gpio.extension.ads.ADS1015GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1015Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider.ProgrammableGainAmplifierValue;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;

import java.io.IOException;
import java.text.DecimalFormat;

public class PollutionSensorADS1015 {

    private I2CDevice device;
    private ADS1015GpioProvider gpioProvider;

    public PollutionSensorADS1015() {
        try {

            System.out.println("<--Pi4J--> ADS1015 GPIO Example ... started.");

            // number formatters
            final DecimalFormat df = new DecimalFormat("#.##");
            final DecimalFormat pdf = new DecimalFormat("###.#");

            // create gpio controller
            final GpioController gpio = GpioFactory.getInstance();

            // create custom ADS1015 GPIO provider
            gpioProvider = new ADS1015GpioProvider(I2CBus.BUS_1, ADS1015GpioProvider.ADS1015_ADDRESS_0x48);

            // provision gpio analog input pins from ADS1015
            GpioPinAnalogInput myInputs[] = {
                    gpio.provisionAnalogInputPin(gpioProvider, ADS1015Pin.INPUT_A0, "MyAnalogInput-A0"),
                    gpio.provisionAnalogInputPin(gpioProvider, ADS1015Pin.INPUT_A1, "MyAnalogInput-A1"),
                    gpio.provisionAnalogInputPin(gpioProvider, ADS1015Pin.INPUT_A2, "MyAnalogInput-A2"),
                    gpio.provisionAnalogInputPin(gpioProvider, ADS1015Pin.INPUT_A3, "MyAnalogInput-A3"),
            };

            gpioProvider.setProgrammableGainAmplifier(ProgrammableGainAmplifierValue.PGA_4_096V, ADS1015Pin.ALL);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int readValue() throws IOException, InterruptedException {
        return (int) gpioProvider.getImmediateValue(ADS1015Pin.INPUT_A0);
    }


}
