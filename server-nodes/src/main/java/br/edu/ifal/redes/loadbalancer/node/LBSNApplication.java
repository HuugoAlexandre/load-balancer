package br.edu.ifal.redes.loadbalancer.node;

public final class LBSNApplication {

    private static final String DEFAULT_HOSTNAME = "127.0.0.1";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Argumento 1 (porta) é necessário.");

            return;
        }

        final int port = Integer.parseInt(args[0]);
        final AppServer server = new AppServer(DEFAULT_HOSTNAME, port);

        server.lockThreadAndStart();
    }

}
