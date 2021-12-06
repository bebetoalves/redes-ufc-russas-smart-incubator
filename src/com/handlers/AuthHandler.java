package com.handlers;

import com.incubator.enums.OperationState;
import com.incubator.interfaces.IGenericActuator;
import com.net.client.ClientType;
import com.net.header.HeaderAction;
import com.net.header.HeaderLength;
import com.net.header.HeaderResponse;
import com.net.server.ServerInterface;
import com.utils.HeaderUtils;

import java.io.DataOutputStream;
import java.io.IOException;

public class AuthHandler {
    private final ServerInterface server;
    private final ClientHandler clientHandler;
    private final DataOutputStream out;

    public AuthHandler(ServerInterface server, ClientHandler clientHandler, DataOutputStream out) {
        this.server = server;
        this.clientHandler = clientHandler;
        this.out = out;
    }

    public void handle(String clientType, String clientId) throws IOException {
        if (server.getClientByName(clientId) != null) {
            String response = HeaderUtils.createMessage(HeaderAction.REJ, clientType, HeaderResponse.ALREADY_CONNECTED);

            clientHandler.sendMessage(out, response);

            System.out.printf("MANA -> %s: %s\n", clientId, response);
            throw new IOException("Client already connected!");
        }

        if (clientId.length() > HeaderLength.CLIENT_ID) {
            String response = HeaderUtils.createMessage(HeaderAction.REJ, clientType, HeaderResponse.INVALID_ID);

            clientHandler.sendMessage(out, response);

            System.out.printf("MANA -> %s: %s\n", clientId, response);
            throw new IOException("Client ID is too long!");
        }

        String response = HeaderUtils.createMessage(HeaderAction.ACK, clientType, clientId);

        clientHandler.setName(clientId);
        clientHandler.sendMessage(out, response);

        // If the client is an actuator, change its state to ON.
        if (ClientType.ACTUATOR.getValue().equals(clientType)) {
            IGenericActuator actuator = server.getIncubator().getActuatorById(clientId);

            if (actuator != null) {
                actuator.setState(OperationState.ON);
            }
        }

        System.out.printf("MANA -> %s: %s\n", clientId, response);
    }
}
