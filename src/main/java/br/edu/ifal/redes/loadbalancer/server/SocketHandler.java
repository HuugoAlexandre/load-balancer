package br.edu.ifal.redes.loadbalancer.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        try (PrintWriter writer = new PrintWriter(client.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            System.out
                    .format(
                            "[INFO] Nova conexão recebida: %s:%s",
                            client.getInetAddress().getHostName(),
                            client.getPort()
                    ).println();

            final String line = reader.readLine();

            if (line == null || !line.equals("lb/connect-server")) {
                final ServerNode node = Server.NODES.next();

                if (node == null) {
                    sendDefaultResponse(writer);

                    return;
                }

                node.forward(client);

                return;
            }

            final String host = reader.readLine();
            final String port = reader.readLine();

            final ServerNode node = new ServerNode(host, Integer.parseInt(port));

            if (!Server.NODES.contains(node)) {
                Server.NODES.add(node);
            }

            System.out.format("[INFO] Novo servidor adicionado ao pool: %s:%s", host, port).println();
            writer.printf("Servidor adicionado com sucesso: %s:%s", host, port);

            writer.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        start();
    }

    private void sendDefaultResponse(PrintWriter writer) {
        final String response = "Hello from Load Balancer.";
        final int length = response.length();

        writer.print("HTTP/1.1 200 OK\r\n");
        writer.print("Content-Type: text/plain\r\n");
        writer.print("Content-Length: " + length + "\r\n");
        writer.print("\r\n");
        writer.print(response);

        writer.flush();
    }
}
