package com.sensors;

import com.Configs;

public class TemperatureSensor extends SensorClient {
    public TemperatureSensor(String id) {
        super(id);
    }

    public static void main(String[] args) {
        SensorClient temperatureSensor = new TemperatureSensor("TEMP");
        temperatureSensor.setValue(20);
        temperatureSensor.start(Configs.HOST_NAME, Configs.PORT);
    }
}
