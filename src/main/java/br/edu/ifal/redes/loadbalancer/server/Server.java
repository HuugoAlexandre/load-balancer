package br.edu.ifal.redes.loadbalancer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // http
    private static final int PORT = 80;
    private final ServerSocket server;

    public Server() throws IOException {
        this.server = new ServerSocket(PORT);
    }

    public void lockThreadAndStart() {
        System.out.format("[INFO] Load Balancer rodando na porta %d", PORT).println();

        while (true) {
            try (Socket socket = this.server.accept()) {
                final ServerHandler handler = new ServerHandler(socket);

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
