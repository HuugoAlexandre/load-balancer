package br.edu.ifal.redes.loadbalancer.backend;

import java.util.concurrent.ConcurrentLinkedDeque;

public class ServerNodeIntegrityRunnable implements Runnable {

    @Override
    public void run() {
        final ConcurrentLinkedDeque<ServerNode> nodes = ServerNodeController.NODES;

        System.out.format("[INFO] Verificando a integridade de %d servidores...", nodes.size()).println();

        for (ServerNode node : nodes) {
            System.out
                    .format("[INFO] Verificando a integridade do servidor %s:%d", node.getHost(), node.getPort())
                    .println();

            if (!node.isAlive()) {
                System.out.println("[WARNING] Servidor não respondeu como esperado, talvez esteja off-line.");

                nodes.remove(node);

                System.out.println("[INFO] Servidor removido do pool de servidores.");
            }
        }

        System.out.
                format("[INFO] Após a verificação de integridade temos %d servidores disponíveis.", nodes.size())
                .println();
    }

}
