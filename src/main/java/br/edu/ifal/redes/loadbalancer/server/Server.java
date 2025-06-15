package br.edu.ifal.redes.loadbalancer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 80; // roda na porta HTTP

    private final ServerSocket server;

    public Server() throws IOException {
        this.server = new ServerSocket(PORT);
    }

    public void lockAndStart() {
        while (true) {
            try (Socket socket = this.server.accept()) {
                final SocketHandler handler = new SocketHandler(socket);

                handler.start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public ServerSocket getServer() {
        return server;
    }

}
