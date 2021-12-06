package com.incubator.interfaces;

public interface IGenericSensor {
    String getId();

    String getValue();

    void setValue(String value);

    Integer getMax();

    Integer getMin();
}
