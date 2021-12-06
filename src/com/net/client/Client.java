package com.net.client;

import com.Configs;
import com.handlers.MessageHandler;
import com.net.header.HeaderAction;
import com.utils.HeaderUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class Client implements ClientInterface {
    private final String id;
    private final ClientType type;
    private Socket socket;
    private DataOutputStream out;

    public Client(String id, ClientType type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public void start(String host, int port) {
        // Connect to server.
        connect(host, port);

        if (getSocket() != null) {
            // Authenticate every client that connects to the server.
            authenticate();

            // Start the client's message thread.
            createMessageThread(this);

            // Execute client's actions.
            while (true) {
                try {
                    if (getSocket().isClosed()) {
                        throw new IOException("Socket has been closed!");
                    }
                    // Execute some actions.
                    execute();
                } catch (Exception e) {
                    System.out.printf("Client [%s] got an error! [%s]\n", getId(), e);
                    stop();
                    break;
                }
            }
        }
    }

    private void connect(String host, int port) {
        while (socket == null) {
            try {
                socket = new Socket(host, port);
                out = new DataOutputStream(socket.getOutputStream());
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void execute() throws Exception {
        System.out.println("Execute client actions.");
    }

    private void authenticate() {
        try {
            String request = HeaderUtils.createMessage(HeaderAction.CON, getType().getValue(), getId());

            getOutputStream().writeUTF(request);
            getOutputStream().flush();

            DataInputStream clientMessage = new DataInputStream(socket.getInputStream());
            String response = clientMessage.readUTF();

            if (!response.equals(HeaderUtils.createMessage(HeaderAction.ACK, getType().getValue(), getId()))) {
                throw new Exception("Invalid credentials or bad header format.");
            }

            System.out.printf("%s -> MANA: %s\n", getId(), request);
            System.out.printf("%s <- MANA: %s\n", getId(), response);
        } catch (Exception e) {
            System.out.printf("Client [%s] could not authenticate! [%s]\n", getId(), e);
            stop();
            System.exit(1);
        }
    }

    public void createMessageThread(ClientInterface client) {
        MessageHandler messageHandler = new MessageHandler(client);
        messageHandler.start();
    }

    @Override
    public void handle(String message) {
        System.out.println("Handle this message: " + message);
    }

    @Override
    public void stop() {
        try {
            if (out != null) {
                out.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.printf("Client [%s] could not close socket! [%s]\n", getId(), e);
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public ClientType getType() {
        return this.type;
    }

    @Override
    public Socket getSocket() {
        return this.socket;
    }

    @Override
    public DataOutputStream getOutputStream() {
        return this.out;
    }
}
