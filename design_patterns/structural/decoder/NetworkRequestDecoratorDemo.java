package design_patterns.structural.decoder;


public class NetworkRequestDecoratorDemo {

    // ==========================
    // 1. Component Interface
    // ==========================
    interface RequestSender {
        void send(String request);
    }

    // ==========================
    // 2. Concrete Component
    // ==========================
    static class BasicRequestSender implements RequestSender {

        @Override
        public void send(String request) {
            System.out.println("Sending request: " + request);
        }
    }

    // ==========================
    // 3. Abstract Decorator
    // ==========================
    static abstract class RequestDecorator implements RequestSender {
        protected RequestSender sender;

        RequestDecorator(RequestSender sender) {
            this.sender = sender;
        }
    }

    // ==========================
    // 4. Concrete Decorators
    // ==========================

    //  Authentication
    static class AuthDecorator extends RequestDecorator {

        AuthDecorator(RequestSender sender) {
            super(sender);
        }

        @Override
        public void send(String request) {
            request = request + " | AuthToken=ABC123";
            sender.send(request);
        }
    }

    //  Retry logic
    static class RetryDecorator extends RequestDecorator {

        private int retries = 3;

        RetryDecorator(RequestSender sender) {
            super(sender);
        }

        @Override
        public void send(String request) {
            for (int i = 1; i <= retries; i++) {
                try {
                    System.out.println("Attempt " + i);
                    sender.send(request);
                    break;
                } catch (Exception e) {
                    System.out.println("Retrying...");
                }
            }
        }
    }

    //  Compression
    static class CompressionDecorator extends RequestDecorator {

        CompressionDecorator(RequestSender sender) {
            super(sender);
        }

        @Override
        public void send(String request) {
            request = "[COMPRESSED]" + request;
            sender.send(request);
        }
    }

    //  Logging
    static class LoggingDecorator extends RequestDecorator {

        LoggingDecorator(RequestSender sender) {
            super(sender);
        }

        @Override
        public void send(String request) {
            System.out.println("LOG: Request is about to be sent");
            sender.send(request);
        }
    }

    // ==========================
    // 5. Client
    // ==========================
    public static void main(String[] args) {

        RequestSender sender = new BasicRequestSender();

        // Build pipeline dynamically
        sender = new AuthDecorator(sender);
        sender = new CompressionDecorator(sender);
        sender = new LoggingDecorator(sender);
        sender = new RetryDecorator(sender);

        sender.send("POST /api/orders");
    }
}