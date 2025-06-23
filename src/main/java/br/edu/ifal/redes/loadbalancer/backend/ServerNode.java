package br.edu.ifal.redes.loadbalancer.backend;

import java.io.*;
import java.net.Socket;

public class ServerNode {

    private final String host;
    private final int port;

    public ServerNode(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void forward(Socket origin) {
        try (final Socket socket = new Socket(host, port)) {
            final Thread clientToServer = new Thread(() -> {
                try {
                    copyStream(origin.getInputStream(), socket.getOutputStream());
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });

            final Thread serverToClient = new Thread(
                () -> {
                    try {
                        copyStream(socket.getInputStream(), origin.getOutputStream());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    } finally {
                        try {
                            origin.close();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            );

            clientToServer.start();
            serverToClient.start();

            clientToServer.join();
            serverToClient.join();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                origin.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void copyStream(InputStream in, OutputStream out) {
        try {
            byte[] buffer = new byte[8192];
            int length;

            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }

            out.flush();
        } catch (IOException e) {
            // ignorando o erro pois eh o esperado mesmo
        }
    }

    public boolean isAlive() {
        try (
                final Socket socket = new Socket(host, port);
                final PrintWriter writer = new PrintWriter(socket.getOutputStream());
                final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            writer.println("integrity");
            writer.flush();

            return reader.readLine() != null;
        } catch (Exception exception) {
            return false;
        }
    }

}
