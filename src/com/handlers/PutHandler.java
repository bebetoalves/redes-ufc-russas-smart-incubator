package com.handlers;

import com.Configs;
import com.incubator.enums.OperationMode;
import com.incubator.enums.OperationState;
import com.incubator.interfaces.IGenericActuator;
import com.incubator.interfaces.IGenericSensor;
import com.net.client.ClientType;
import com.net.header.HeaderAction;
import com.net.header.HeaderResponse;
import com.net.server.ServerInterface;
import com.utils.HeaderUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PutHandler {
    private final ServerInterface server;
    private final ClientHandler clientHandler;
    private final DataOutputStream out;

    public PutHandler(ServerInterface server, ClientHandler clientHandler, DataOutputStream out) {
        this.server = server;
        this.clientHandler = clientHandler;
        this.out = out;
    }

    public void handle(String key, String value) throws IOException {
        if (server.getIncubator().getClientTypeById(key).equals(ClientType.SENSOR)) {
            handleSensorPut(key, value);
            handleSensorLimits(key, value);
        }

        if (server.getIncubator().getClientTypeById(key).equals(ClientType.ACTUATOR)) {
            handleActuatorPut(key, value);
        }
    }

    private void handleSensorPut(String key, String value) throws IOException {
        server.getIncubator().updateSensorValue(key, value);
        String response = HeaderUtils.createMessage(HeaderAction.ACK, key, value);
        clientHandler.sendMessage(out, response);
        System.out.printf("MANA -> %s: %s\n", key, response);
    }

    private void handleActuatorPut(String key, String value) throws IOException {
        IGenericActuator actuator = server.getIncubator().getActuatorById(key);
        String sensorId = actuator.getSensor().getId();
        String operationMode = actuator.getOperationMode().toString();
        ClientHandler sensorHandler = server.getClientByName(sensorId);

        String response;

        if (sensorHandler != null) {
            Socket sensorSocket = sensorHandler.getSocket();

            response = HeaderUtils.createMessage(HeaderAction.ACK, key, value);
            clientHandler.sendMessage(out, response);

            DataOutputStream outToSensor = new DataOutputStream(sensorSocket.getOutputStream());
            String request = HeaderUtils.createMessage(HeaderAction.PUT, sensorId, operationMode);
            clientHandler.sendMessage(outToSensor, request);

            System.out.printf("MANA -> %s: %s\n", sensorId, request);
        } else {
            response = HeaderUtils.createMessage(HeaderAction.REJ, key, HeaderResponse.SENSOR_OFF);
            clientHandler.sendMessage(out, response);
        }
        System.out.printf("MANA -> %s: %s\n", key, response);
    }

    private void handleSensorLimits(String sensorId, String value) throws IOException {
        IGenericSensor sensor = server.getIncubator().getSensorById(sensorId);
        Integer sensorMax = sensor.getMax();
        Integer sensorMin = sensor.getMin();

        int totalActuators = server.getIncubator().getAllActuatorsBySensorId(sensorId).size();

        if (totalActuators > 0) {
            if (Integer.parseInt(value) > sensorMax) {
                // Turn off the culprit actuator.
                calibrateActuator(sensorId, OperationMode.UP, OperationState.OFF);
                // Turn on the opposite actuator.
                calibrateActuator(sensorId, OperationMode.DOWN, OperationState.ON);
            }

            if (Integer.parseInt(value) < sensorMin) {
                // Turn off the culprit actuator.
                calibrateActuator(sensorId, OperationMode.DOWN, OperationState.OFF);
                // Turn on the opposite actuator.
                calibrateActuator(sensorId, OperationMode.UP, OperationState.ON);
            }
        } else {
            if (Integer.parseInt(value) > sensorMax || Integer.parseInt(value) < sensorMin) {
                String monitorId = Configs.MONITOR_ID;
                ClientHandler monitorHandler = server.getClientByName(monitorId);

                if (monitorHandler != null) {
                    Socket monitorSocket = monitorHandler.getSocket();
                    DataOutputStream outToMonitor = new DataOutputStream(monitorSocket.getOutputStream());

                    String response = HeaderUtils.createMessage(HeaderAction.ERR, monitorId, sensorId);
                    clientHandler.sendMessage(outToMonitor, response);

                    System.out.printf("MANA -> %s: %s\n", monitorId, response);
                }
            }
        }
    }

    private void calibrateActuator(String sensorId, OperationMode mode, OperationState state) throws IOException {
        IGenericActuator actuator = server.getIncubator().getActuatorBySensorId(sensorId, mode);

        // If null, means the sensorId is not connected to an actuator.
        if (actuator == null) {
            return;
        }

        String actuatorId = actuator.getId();
        ClientHandler actuatorHandler = server.getClientByName(actuatorId);

        if (actuatorHandler != null) {
            if (actuator.getState().equals(state)) {
                return;
            }

            // Change the actuator state.
            actuator.setState(state);

            DataOutputStream out = new DataOutputStream(actuatorHandler.getSocket().getOutputStream());

            // Send the new state to the actuator.
            String response = HeaderUtils.createMessage(HeaderAction.PUT, actuatorId, state.toString());
            clientHandler.sendMessage(out, response);

            System.out.printf("MANA -> %s: %s\n", actuatorId, response);
        } else {
            System.out.printf("Actuator for sensor [%s] is not connected!\n", sensorId);
        }
    }
}
