package com.incubator;

import com.incubator.enums.OperationMode;
import com.incubator.enums.OperationState;
import com.incubator.interfaces.IGenericActuator;
import com.incubator.interfaces.IGenericSensor;

public class GenericActuator implements IGenericActuator {
    private final String id;
    private final IGenericSensor sensor;
    private final OperationMode mode;
    private OperationState state = OperationState.OFF;

    public GenericActuator(String id, IGenericSensor sensor, OperationMode mode) {
        this.id = id;
        this.sensor = sensor;
        this.mode = mode;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public IGenericSensor getSensor() {
        return this.sensor;
    }

    @Override
    public OperationMode getOperationMode() {
        return this.mode;
    }

    @Override
    public OperationState getState() {
        return this.state;
    }

    @Override
    public void setState(OperationState state) {
        this.state = state;
    }
}
