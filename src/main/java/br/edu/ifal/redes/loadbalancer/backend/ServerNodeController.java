package br.edu.ifal.redes.loadbalancer.backend;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class ServerNodeController {

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
    public static final ConcurrentLinkedDeque<ServerNode> NODES = new ConcurrentLinkedDeque<>();

    public static ServerNode nextServer() {
        if (NODES.isEmpty()) {
            return null;
        }

        final ServerNode current = NODES.pollFirst();

        NODES.addLast(current);

        return current;
    }

    public static void putServer(ServerNode node) {
        final boolean exists = NODES
                .stream()
                .anyMatch((server) -> server.getPort() == node.getPort());

        if (!exists) {
            System.out.format("[INFO] Novo servidor adicionado: %s:%d", node.getHost(), node.getPort()).println();

            NODES.addLast(node);
        }
    }

    public static void startNodeIntegrityCheck() {
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(
                new ServerNodeIntegrityRunnable(),
                15,
                15,
                TimeUnit.SECONDS
        );
    }

}
