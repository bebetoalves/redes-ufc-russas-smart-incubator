package com.handlers;

import com.net.header.HeaderAction;
import com.net.header.HeaderArg;
import com.net.header.HeaderResponse;
import com.net.server.ServerInterface;
import com.utils.HeaderUtils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final ServerInterface server;

    public ClientHandler(Socket socket, ServerInterface server) {
        this.socket = socket;
        this.server = server;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        try {
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            DataInputStream dis = new DataInputStream(bis);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            while (true) {
                try {
                    String messageFromClient = dis.readUTF();

                    if (!HeaderUtils.isValidRequest(messageFromClient)) {
                        String response = HeaderUtils.createMessage(HeaderAction.REJ, HeaderResponse.UNKNOWN_ID, HeaderResponse.BAD_REQUEST);
                        sendMessage(out, response);
                        System.out.printf("MANA -> UNKNOWN: %s\n", response);
                        break;
                    }

                    String[] args = HeaderUtils.convertToArgs(messageFromClient);
                    String action = args[HeaderArg.ACTION];
                    String key = args[HeaderArg.KEY];
                    String value = args[HeaderArg.VALUE];

                    if (action.equals(HeaderAction.CON.toString())) {
                        System.out.printf("MANA <- UNKNOWN: %s\n", messageFromClient);
                        new AuthHandler(server, this, out).handle(key, value);
                    }

                    System.out.printf("MANA <- %s: %s\n", key, messageFromClient);

                    if (action.equals(HeaderAction.GET.toString())) {
                        new GetHandler(server, this, out).handle(key);
                    }

                    if (action.equals(HeaderAction.PUT.toString())) {
                        new PutHandler(server, this, out).handle(key, value);
                    }
                    out.flush();
                } catch (IOException e) {
                    break;
                }
            }

            out.close();
            dis.close();
            bis.close();
            socket.close();
            server.removeClient(this);

            System.out.printf("Client from IP [%s] disconnect from server.\n", socket.getRemoteSocketAddress());
        } catch (IOException e) {
            System.out.printf("Client Handler [%s] got an error! [%s]\n", getName(), e);
        }
    }

    public void sendMessage(DataOutputStream dataOutputStream, String message) throws IOException {
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
    }
}
