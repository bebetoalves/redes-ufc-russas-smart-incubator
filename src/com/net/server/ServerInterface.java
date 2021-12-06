package com.net.server;

import com.handlers.ClientHandler;
import com.incubator.interfaces.IGenericIncubator;

import java.net.ServerSocket;
import java.util.List;

public interface ServerInterface {
    ServerSocket getServerSocket();

    List<ClientHandler> getClients();

    void removeClient(ClientHandler clientHandler);

    ClientHandler getClientByName(String name);

    IGenericIncubator getIncubator();

    void setIncubator(IGenericIncubator incubator);
}
