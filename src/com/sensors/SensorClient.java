package com.sensors;

import com.Configs;
import com.incubator.enums.OperationMode;
import com.net.client.Client;
import com.net.client.ClientType;
import com.net.header.HeaderAction;
import com.net.header.HeaderArg;
import com.utils.HeaderUtils;

public class SensorClient extends Client {
    private Integer value = 0;

    public SensorClient(String id) {
        super(id, ClientType.SENSOR);
    }

    @Override
    public void execute() throws Exception {
        String request = HeaderUtils.createMessage(HeaderAction.PUT, getId(), getValue().toString());

        getOutputStream().writeUTF(request);
        getOutputStream().flush();

        System.out.printf("%s -> MANA: %s\n", getId(), request);
        Thread.sleep(Configs.TIMEOUT);
    }

    @Override
    public void handle(String response) {
        System.out.printf("%s <- MANA: %s\n", getId(), response);

        String[] args = HeaderUtils.convertToArgs(response);
        String value = args[HeaderArg.VALUE];

        if (value.equals(OperationMode.UP.toString())) {
            this.value++;
        }

        if (value.equals(OperationMode.DOWN.toString())) {
            if (this.value > 0) {
                this.value--;
            }
        }
    }

    public Integer getValue() {
        return this.value;
    }

    protected void setValue(Integer value) {
        this.value = value;
    }
}
