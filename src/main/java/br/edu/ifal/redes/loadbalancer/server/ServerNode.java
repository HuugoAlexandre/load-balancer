package br.edu.ifal.redes.loadbalancer.server;

import java.io.InputStream;
import java.io.OutputStream;
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
        try (Socket socket = new Socket(host, port)) {
            final InputStream inputStream = origin.getInputStream();
            final OutputStream outputStream = socket.getOutputStream();

            byte[] buffer = new byte[8192];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
                outputStream.flush();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
