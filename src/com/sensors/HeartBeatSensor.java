package com.sensors;

import com.Configs;

import java.util.Random;

public class HeartBeatSensor extends SensorClient {
    public HeartBeatSensor(String id) {
        super(id);
    }

    public static void main(String[] args) {
        new HeartBeatSensor("BEAT").start(Configs.HOST_NAME, Configs.PORT);
    }

    @Override
    public Integer getValue() {
        Random rand = new Random();

        int minValue = 59;
        int maxValue = 76;

        return minValue + rand.nextInt((maxValue - minValue) + 1);
    }
}
