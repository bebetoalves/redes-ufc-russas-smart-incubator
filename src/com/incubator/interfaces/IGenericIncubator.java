package com.incubator.interfaces;

import com.incubator.enums.OperationMode;
import com.net.client.ClientType;

import java.util.List;

public interface IGenericIncubator {
    List<IGenericSensor> getSensors();

    void addSensor(IGenericSensor sensor);

    List<IGenericActuator> getActuators();

    void addActuator(IGenericActuator actuator);

    ClientType getClientTypeById(String id);

    void updateSensorValue(String id, String value);

    String getSensorValue(String id);

    IGenericSensor getSensorById(String sensorId);

    IGenericActuator getActuatorById(String id);

    IGenericActuator getActuatorBySensorId(String sensorId, OperationMode mode);

    List<IGenericActuator> getAllActuatorsBySensorId(String sensorId);
}
