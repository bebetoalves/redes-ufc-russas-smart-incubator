package com.handlers;

import com.Configs;
import com.incubator.interfaces.IGenericSensor;
import com.net.header.HeaderAction;
import com.net.server.ServerInterface;
import com.utils.HeaderUtils;

import java.io.DataOutputStream;
import java.io.IOException;

public class GetHandler {
    private final ServerInterface server;
    private final ClientHandler clientHandler;
    private final DataOutputStream out;

    public GetHandler(ServerInterface server, ClientHandler clientHandler, DataOutputStream out) {
        this.server = server;
        this.clientHandler = clientHandler;
        this.out = out;
    }

    public void handle(String clientId) throws IOException {
        if (clientId.equals(Configs.MONITOR_ID)) {
            for (IGenericSensor sensor : server.getIncubator().getSensors()) {
                String response = HeaderUtils.createMessage(HeaderAction.ACK, sensor.getId(), sensor.getValue());
                clientHandler.sendMessage(out, response);

                System.out.printf("MANA -> %s: %s\n", clientId, response);
            }
        }
    }
}
