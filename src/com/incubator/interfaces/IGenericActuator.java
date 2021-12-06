package com.incubator.interfaces;

import com.incubator.enums.OperationMode;
import com.incubator.enums.OperationState;

public interface IGenericActuator {
    String getId();

    IGenericSensor getSensor();

    OperationMode getOperationMode();

    OperationState getState();

    void setState(OperationState state);
}
