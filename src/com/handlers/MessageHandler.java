package com.handlers;

import com.net.client.ClientInterface;

import java.io.DataInputStream;
import java.io.IOException;

public class MessageHandler extends Thread {
    private final ClientInterface client;

    public MessageHandler(ClientInterface client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(client.getSocket().getInputStream());
            while (true) {
                try {
                    client.handle(dis.readUTF());
                } catch (IOException e) {
                    break;
                }
            }
            dis.close();
        } catch (Exception e) {
            System.out.printf("Client [%s] is unable to read messages! [%s]\n", client.getId(), e);
            client.stop();
        }
    }
}
