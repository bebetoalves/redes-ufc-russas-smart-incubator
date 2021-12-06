package com.sensors;

import com.Configs;

import java.util.Random;

public class OxygenSensor extends SensorClient {
    public OxygenSensor(String id) {
        super(id);
    }

    public static void main(String[] args) {
        new OxygenSensor("OXYL").start(Configs.HOST_NAME, Configs.PORT);
    }

    @Override
    public Integer getValue() {
        Random rand = new Random();

        int minValue = 89;
        int maxValue = 97;

        return minValue + rand.nextInt((maxValue - minValue) + 1);
    }
}
