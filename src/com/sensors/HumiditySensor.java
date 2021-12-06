package com.sensors;

import com.Configs;

public class HumiditySensor extends SensorClient {
    public HumiditySensor(String id) {
        super(id);
    }

    public static void main(String[] args) {
        SensorClient temperatureSensor = new HumiditySensor("HUMY");
        temperatureSensor.setValue(14);
        temperatureSensor.start(Configs.HOST_NAME, Configs.PORT);
    }
}
