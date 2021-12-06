package com.net.server;

import com.handlers.ClientHandler;
import com.incubator.interfaces.IGenericIncubator;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server implements ServerInterface {
    private final List<ClientHandler> clientHandlers = new ArrayList<>();
    private ServerSocket serverSocket;
    private IGenericIncubator incubator;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.printf("Server is running on port: %s.\n", port);
        } catch (Exception e) {
            System.out.printf("Server could not create server socket! [%s]\n", e);
        }
    }

    public void start() {
        while (serverSocket.isBound()) {
            try {
                ClientHandler clientHandler = new ClientHandler(serverSocket.accept(), this);
                clientHandler.start();

                clientHandlers.add(clientHandler);
            } catch (IOException e) {
                System.out.printf("Server could not accept client! [%s]\n", e);
            }
        }
    }

    @Override
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public List<ClientHandler> getClients() {
        return this.clientHandlers;
    }

    @Override
    public void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    @Override
    public ClientHandler getClientByName(String name) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.getName().equals(name)) {
                return clientHandler;
            }
        }

        return null;
    }

    @Override
    public IGenericIncubator getIncubator() {
        return this.incubator;
    }

    @Override
    public void setIncubator(IGenericIncubator incubator) {
        this.incubator = incubator;
    }
}
