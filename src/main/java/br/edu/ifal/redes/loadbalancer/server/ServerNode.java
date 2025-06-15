package br.edu.ifal.redes.loadbalancer.server;

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
            // copyStream(origin.getInputStream(), socket.getOutputStream());
            copyStream(socket.getInputStream(), origin.getOutputStream());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void copyStream(InputStream in, OutputStream out) {
        try {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
