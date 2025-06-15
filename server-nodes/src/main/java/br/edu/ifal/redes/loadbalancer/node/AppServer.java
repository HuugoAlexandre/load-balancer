package br.edu.ifal.redes.loadbalancer.node;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AppServer {

    private static final String LOAD_BALANCER_HOSTNAME = "127.0.0.1";
    private static final int LOAD_BALANCER_PORT = 80;

    private final String hostname;
    private final int port;

    public AppServer(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public void lockThreadAndStart() {
        connectNode();

        try (final ServerSocket server = new ServerSocket(port)) {
            System.out.println("[INFO] Servidor aberto na porta " + port + ".");

            while (true) {
                try (
                        final Socket socket = server.accept();
                        final PrintWriter writer = new PrintWriter(socket.getOutputStream());
                ) {
                    System.out
                            .format(
                                    "[INFO] Nova conexão recebida: %s:%s",
                                    socket.getInetAddress().getHostAddress(),
                                    socket.getPort()
                            ).println();

                    final String response = "Hello from Server Node at " + port + ".";
                    final int length = response.length();

                    writer.println("HTTP/1.1 200 OK");
                    writer.println("Content-Type: text/plain");
                    writer.println("Content-Length: " + length);
                    writer.println();
                    writer.println(response);

                    writer.flush();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void connectNode() {
        System.out.println("Tentando conectar ao load balancer...");

        try (final Socket socket = new Socket(LOAD_BALANCER_HOSTNAME, LOAD_BALANCER_PORT);
             final PrintWriter writer = new PrintWriter(socket.getOutputStream());
             final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            writer.println("lb/connect-server");
            writer.println(hostname);
            writer.println(port);
            writer.flush();

            final String response = reader.readLine();

            System.out.println(response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        System.out.println("Fim da tentativa de conexão.");
    }

}
