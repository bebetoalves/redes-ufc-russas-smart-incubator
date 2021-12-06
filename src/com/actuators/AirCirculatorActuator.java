package com.actuators;

import com.Configs;
import com.incubator.enums.OperationMode;
import com.incubator.enums.OperationState;

public class AirCirculatorActuator extends BaseActuator {
    public AirCirculatorActuator(String id) {
        super(id, OperationMode.DOWN, OperationState.ON);
    }

    public static void main(String[] args) {
        new AirCirculatorActuator("AIRC").start(Configs.HOST_NAME, Configs.PORT);
    }
}
