package com.incubator;

import com.incubator.interfaces.IGenericSensor;

public class GenericSensor implements IGenericSensor {
    private final String id;
    private final Integer max;
    private final Integer min;
    private String value;

    public GenericSensor(String id, Integer min, Integer max) {
        this.id = id;
        this.min = min;
        this.max = max;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Integer getMax() {
        return this.max;
    }

    @Override
    public Integer getMin() {
        return this.min;
    }
}
