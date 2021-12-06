package com;

import com.incubator.GenericActuator;
import com.incubator.GenericSensor;
import com.incubator.Incubator;
import com.incubator.enums.OperationMode;
import com.incubator.interfaces.IGenericActuator;
import com.incubator.interfaces.IGenericIncubator;
import com.incubator.interfaces.IGenericSensor;
import com.net.server.Server;

public class Manager {
    public static void main(String[] args) {
        // Create sensors.
        IGenericSensor temperature = new GenericSensor("TEMP", 18, 30);
        IGenericSensor humidity = new GenericSensor("HUMY", 20, 40);
        IGenericSensor oxygenLevel = new GenericSensor("OXYL", 90, 97);
        IGenericSensor heartBeat = new GenericSensor("BEAT", 60, 75);

        // Create actuators.
        IGenericActuator heat = new GenericActuator("HEAT", temperature, OperationMode.UP);
        IGenericActuator humidifier = new GenericActuator("HUMR", humidity, OperationMode.UP);
        IGenericActuator airCirculator = new GenericActuator("AIRC", temperature, OperationMode.DOWN);

        // Create the incubator.
        IGenericIncubator incubator = new Incubator();

        // Add sensors to the incubator.
        incubator.addSensor(temperature);
        incubator.addSensor(humidity);
        incubator.addSensor(oxygenLevel);
        incubator.addSensor(heartBeat);

        // Add actuators to the incubator.
        incubator.addActuator(heat);
        incubator.addActuator(humidifier);
        incubator.addActuator(airCirculator);

        // Create the server.
        Server server = new Server(2323);

        // Add the incubator to the server.
        server.setIncubator(incubator);

        // Start the server.
        server.start();
    }
}