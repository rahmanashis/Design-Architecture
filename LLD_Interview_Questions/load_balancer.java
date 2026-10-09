import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class load_balancer {

    // =========================
    // Request
    // =========================
    static class Request {
        private final String id;

        public Request(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    // =========================
    // Server
    // =========================
    static class Server {

        private final String id;
        private final String ip;
        private final AtomicInteger activeConnections;
        private volatile boolean isHealthy;

        public Server(String id, String ip) {
            this.id = id;
            this.ip = ip;
            this.activeConnections = new AtomicInteger(0);
            this.isHealthy = true;
        }

        public String getId() {
            return id;
        }

        public boolean isHealthy() {
            return isHealthy;
        }

        public void setHealthy(boolean healthy) {
            this.isHealthy = healthy;
        }

        public int getActiveConnections() {
            return activeConnections.get();
        }

        public void incrementConnections() {
            activeConnections.incrementAndGet();
        }

        public void decrementConnections() {
            activeConnections.decrementAndGet();
        }

        public void handleRequest(Request request) {

            incrementConnections();

            System.out.println(
                Thread.currentThread().getName() +
                " → Request " + request.getId() +
                " handled by Server " + id
            );

            // simulate processing
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}

            decrementConnections();
        }
    }

    // =========================
    // Strategy Interface
    // =========================
    interface LoadBalancingStrategy {

        Server selectServer(List<Server> servers);
    }

    // =========================
    // Round Robin Strategy
    // =========================
    static class RoundRobinStrategy implements LoadBalancingStrategy {

        private final AtomicInteger index = new AtomicInteger(0);

        @Override
        public Server selectServer(List<Server> servers) {

            int size = servers.size();

            for (int i = 0; i < size; i++) {

                int currentIndex =
                    Math.abs(index.getAndIncrement() % size);

                Server server = servers.get(currentIndex);

                if (server.isHealthy())
                    return server;
            }

            throw new RuntimeException("No healthy servers");
        }
    }

    // =========================
    // Least Connections Strategy
    // =========================
    static class LeastConnectionsStrategy
        implements LoadBalancingStrategy {

        @Override
        public Server selectServer(List<Server> servers) {

            Server best = null;

            for (Server server : servers) {

                if (!server.isHealthy())
                    continue;

                if (best == null ||
                    server.getActiveConnections() <
                    best.getActiveConnections()) {

                    best = server;
                }
            }

            if (best == null)
                throw new RuntimeException("No healthy servers");

            return best;
        }
    }

    // =========================
    // LoadBalancer
    // =========================
    static class LoadBalancer {

        private final List<Server> servers;
        private LoadBalancingStrategy strategy;

        public LoadBalancer(LoadBalancingStrategy strategy) {

            this.servers = new CopyOnWriteArrayList<>();
            this.strategy = strategy;
        }

        public void addServer(Server server) {

            servers.add(server);
            System.out.println("Added server: " + server.getId());
        }

        public void removeServer(Server server) {

            servers.remove(server);
        }

        public void setStrategy(
            LoadBalancingStrategy strategy) {

            this.strategy = strategy;
        }

        public void handleRequest(Request request) {

            Server server =
                strategy.selectServer(servers);

            server.handleRequest(request);
        }
    }

    // =========================
    // Client Simulation
    // =========================
    public static void main(String[] args) {

        LoadBalancer lb =
            new LoadBalancer(
                new RoundRobinStrategy()
            );

        lb.addServer(new Server("S1", "192.168.1.1"));
        lb.addServer(new Server("S2", "192.168.1.2"));
        lb.addServer(new Server("S3", "192.168.1.3"));

        // simulate concurrent requests
        for (int i = 1; i <= 10; i++) {

            int requestId = i;

            new Thread(() -> {

                lb.handleRequest(
                    new Request("REQ-" + requestId)
                );

            }).start();
        }
    }
}