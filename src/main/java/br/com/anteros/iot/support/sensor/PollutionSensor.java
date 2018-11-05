package br.com.anteros.iot.support.sensor;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class PollutionSensor {

    private I2CDevice device;

    public PollutionSensor() {
        try {
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
            device = bus.getDevice(0x04);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int readValue() throws IOException, InterruptedException {
        System.out.println("Sending 1");
        device.write((byte) '1');
        Thread.sleep(1000);
        return device.read();
    }


}
