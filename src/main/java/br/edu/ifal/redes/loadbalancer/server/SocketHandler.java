package br.edu.ifal.redes.loadbalancer.server;

import java.io.PrintWriter;
import java.net.Socket;

public class SocketHandler extends Thread {

    private final Socket client;

    public SocketHandler(Socket client) {
        this.client = client;
    }

    public Socket getClient() {
        return client;
    }

    @Override
    public void start() {
        try (PrintWriter writer = new PrintWriter(client.getOutputStream())) {
            final String response = "Hello World";
            final int length = response.length();

            writer.print("HTTP/1.1 200 OK\r\n");
            writer.print("Content-Type: text/plain\r\n");
            writer.print("Content-Length: " + length + "\r\n");
            writer.print("\r\n");
            writer.print(response);
            writer.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        start();
    }
}
