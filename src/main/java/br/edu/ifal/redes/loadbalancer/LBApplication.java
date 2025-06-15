package br.edu.ifal.redes.loadbalancer;

import br.edu.ifal.redes.loadbalancer.server.Server;

public final class LBApplication {

    public static void main(String[] args) {
        try {
            final Server server = new Server();

            server.lockAndStart();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
