package com.actuators;

import com.Configs;
import com.incubator.enums.OperationMode;
import com.incubator.enums.OperationState;
import com.net.client.Client;
import com.net.client.ClientType;
import com.net.header.HeaderAction;
import com.net.header.HeaderArg;
import com.utils.HeaderUtils;

import java.io.IOException;
import java.util.Random;

public class BaseActuator extends Client {
    private final OperationMode mode;
    private OperationState state;

    public BaseActuator(String id, OperationMode mode, OperationState state) {
        super(id, ClientType.ACTUATOR);
        this.mode = mode;
        this.state = state;
    }

    private boolean randomBoolean() {
        Random random = new Random();
        return random.nextInt(4) == 0;
    }

    @Override
    public void execute() throws Exception {
        if (getSocket().isClosed()) {
            throw new IOException("Socket is closed.");
        }

        if (state.equals(OperationState.OFF)) {
            System.out.printf("Actuator [%s] is OFF!\n", getId());
        }

        if (state.equals(OperationState.ON) && randomBoolean()) {
            String request = HeaderUtils.createMessage(HeaderAction.PUT, getId(), mode.toString());

            getOutputStream().writeUTF(request);
            getOutputStream().flush();

            System.out.printf("%s -> MANA: %s\n", getId(), request);
        }

        // Flush the output stream.
        getOutputStream().flush();

        // Sleep for a while.
        Thread.sleep(Configs.TIMEOUT);
    }

    @Override
    public void handle(String response) {
        String[] args = HeaderUtils.convertToArgs(response);
        String value = args[HeaderArg.VALUE];

        System.out.printf("%s <- MANA: %s\n", getId(), response);

        if (value.equals(OperationState.ON.toString())) {
            this.state = OperationState.ON;
        }

        if (value.equals(OperationState.OFF.toString())) {
            this.state = OperationState.OFF;
        }
    }
}
