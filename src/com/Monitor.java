package com;

import com.net.client.Client;
import com.net.client.ClientType;
import com.net.header.HeaderAction;
import com.net.header.HeaderArg;
import com.utils.HeaderUtils;

public class Monitor extends Client {
    public Monitor(String id, ClientType type) {
        super(id, type);
    }

    public static void main(String[] args) {
        new Monitor(Configs.MONITOR_ID, ClientType.MONITOR).start(Configs.HOST_NAME, 2323);
    }

    @Override
    public void execute() throws Exception {
        String request = HeaderUtils.createMessage(HeaderAction.GET, getId(), ClientType.SENSOR.toString());

        getOutputStream().writeUTF(request);
        getOutputStream().flush();

        System.out.printf("%s -> MANA: %s\n", getId(), request);
        Thread.sleep(Configs.TIMEOUT);
    }

    @Override
    public void handle(String message) {
        if (message.contains(HeaderAction.ERR.toString())) {
            String[] args = HeaderUtils.convertToArgs(message);
            String value = args[HeaderArg.VALUE];

            System.out.printf("Warning! Sensor [%s] value is out of range!\n", value);
        }

        if (message.contains(HeaderAction.ACK.toString())) {
            System.out.printf("%s <- MANA: %s\n", getId(), message);
        }
    }
}
