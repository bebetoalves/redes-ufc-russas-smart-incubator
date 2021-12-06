package com.actuators;

import com.Configs;
import com.incubator.enums.OperationMode;
import com.incubator.enums.OperationState;

public class HumidifierActuator extends BaseActuator {
    public HumidifierActuator(String id) {
        super(id, OperationMode.UP, OperationState.ON);
    }

    public static void main(String[] args) {
        new HumidifierActuator("HUMR").start(Configs.HOST_NAME, Configs.PORT);
    }
}
