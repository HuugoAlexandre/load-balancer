package br.edu.ifal.redes.loadbalancer.server;

import br.edu.ifal.redes.loadbalancer.backend.ServerNode;
import br.edu.ifal.redes.loadbalancer.backend.ServerNodeController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandler {

    private final Socket client;

    public ServerHandler(Socket client) {
        this.client = client;
    }

    public void handle() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            final String line = reader.readLine();

            if (line != null && line.equals("node")) {
                addServer(reader);

                return;
            }

            forward();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void forward() {
        final ServerNode node = ServerNodeController.nextServer();

        if (node == null) {
            System.out.println("[WARNING] Uma conexão foi perdida pois não há servidores disponíveis para atendê-la.");

            try (PrintWriter writer = new PrintWriter(client.getOutputStream())) {
                final String response = "Hello from Load Balancer";
                final int length = response.length();

                writer.println("HTTP/1.1 200 OK");
                writer.println("Content-Type: text/plain");
                writer.println("Content-Length: " + length);
                writer.println(); // termina os cabeçalhos http
                writer.print(response);

                writer.flush(); // envia de fato
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return;
        }

        node.forward(client);
    }

    private void addServer(BufferedReader reader) throws IOException {
        final String host = reader.readLine();
        final String port = reader.readLine();

        final ServerNode node = new ServerNode(host, Integer.parseInt(port));

        try (PrintWriter writer = new PrintWriter(client.getOutputStream())) {
            ServerNodeController.putServer(node);

            writer.println("[LOAD-BALANCER -> NODE] Servidor adicionado ao pool de servidores.");
            writer.flush();
        }




    }

}
