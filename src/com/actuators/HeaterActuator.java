package com.actuators;

import com.Configs;
import com.incubator.enums.OperationMode;
import com.incubator.enums.OperationState;

public class HeaterActuator extends BaseActuator {
    public HeaterActuator(String id) {
        super(id, OperationMode.UP, OperationState.ON);
    }

    public static void main(String[] args) {
        new HeaterActuator("HEAT").start(Configs.HOST_NAME, Configs.PORT);
    }
}
