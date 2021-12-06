package com.net.client;

import java.io.DataOutputStream;
import java.net.Socket;

public interface ClientInterface {
    Socket getSocket();

    String getId();

    ClientType getType();

    DataOutputStream getOutputStream();


    void start(String host, int port);

    void stop();

    void execute() throws Exception;

    void handle(String message);
}
