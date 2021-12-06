package com.incubator;

import com.incubator.enums.OperationMode;
import com.incubator.interfaces.IGenericActuator;
import com.incubator.interfaces.IGenericIncubator;
import com.incubator.interfaces.IGenericSensor;
import com.net.client.ClientType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Incubator implements IGenericIncubator {
    private final List<IGenericSensor> sensors = Collections.synchronizedList(new ArrayList<>());
    private final List<IGenericActuator> actuators = Collections.synchronizedList(new ArrayList<>());

    @Override
    public List<IGenericSensor> getSensors() {
        return this.sensors;
    }

    @Override
    public void addSensor(IGenericSensor sensor) {
        this.sensors.add(sensor);
    }

    @Override
    public List<IGenericActuator> getActuators() {
        return this.actuators;
    }

    @Override
    public void addActuator(IGenericActuator actuator) {
        this.actuators.add(actuator);
    }

    @Override
    public ClientType getClientTypeById(String id) {
        for (IGenericSensor sensor : this.sensors) {
            if (sensor.getId().equals(id)) {
                return ClientType.SENSOR;
            }
        }

        for (IGenericActuator actuator : this.actuators) {
            if (actuator.getId().equals(id)) {
                return ClientType.ACTUATOR;
            }
        }

        return null;
    }

    @Override
    public void updateSensorValue(String id, String value) {
        for (IGenericSensor sensor : this.sensors) {
            if (sensor.getId().equals(id)) {
                sensor.setValue(value);
            }
        }
    }

    @Override
    public String getSensorValue(String id) {
        for (IGenericSensor sensor : this.sensors) {
            if (sensor.getId().equals(id)) {
                return sensor.getValue();
            }
        }
        return null;
    }

    @Override
    public IGenericSensor getSensorById(String sensorId) {
        for (IGenericSensor sensor : this.sensors) {
            if (sensor.getId().equals(sensorId)) {
                return sensor;
            }
        }

        return null;
    }

    @Override
    public IGenericActuator getActuatorById(String id) {
        for (IGenericActuator actuator : this.actuators) {
            if (actuator.getId().equals(id)) {
                return actuator;
            }
        }

        return null;
    }

    @Override
    public IGenericActuator getActuatorBySensorId(String sensorId, OperationMode operationMode) {
        for (IGenericActuator actuator : this.actuators) {
            if (actuator.getSensor().getId().equals(sensorId) && actuator.getOperationMode().equals(operationMode)) {
                return actuator;
            }
        }

        return null;
    }

    @Override
    public List<IGenericActuator> getAllActuatorsBySensorId(String sensorId) {
        List<IGenericActuator> actuators = new ArrayList<>();
        for (IGenericActuator actuator : this.actuators) {
            if (actuator.getSensor().getId().equals(sensorId)) {
                actuators.add(actuator);
            }
        }

        return actuators;
    }
}
