package br.edu.ifal.redes.loadbalancer;

import br.edu.ifal.redes.loadbalancer.backend.ServerNodeController;
import br.edu.ifal.redes.loadbalancer.server.Server;

public final class LBApplication {

    public static void main(String[] args) {
        ServerNodeController.startNodeIntegrityCheck();

        try {
            final Server server = new Server();

            server.lockThreadAndStart();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
