package design_patterns.structural.decoder;
public class DecoratorLoggingDemo {

    // =======================
    // 1. Component Interface
    // =======================
    interface Logger {
        void log(String message);
    }

    // =======================
    // 2. Concrete Component
    // =======================
    static class ConsoleLogger implements Logger {
        @Override
        public void log(String message) {
            System.out.println(message);
        }
    }

    // =======================
    // 3. Abstract Decorator
    // =======================
    static abstract class LoggerDecorator implements Logger {
        protected Logger logger;

        LoggerDecorator(Logger logger) {
            this.logger = logger;
        }
    }

    // =======================
    // 4. Concrete Decorators
    // =======================

    // Adds timestamp
    static class TimestampLogger extends LoggerDecorator {

        TimestampLogger(Logger logger) {
            super(logger);
        }

        @Override
        public void log(String message) {
            String msg = System.currentTimeMillis() + " : " + message;
            logger.log(msg);
        }
    }

    // Adds log level
    static class LevelLogger extends LoggerDecorator {
        private String level;

        LevelLogger(Logger logger, String level) {
            super(logger);
            this.level = level;
        }

        @Override
        public void log(String message) {
            logger.log("[" + level + "] " + message);
        }
    }

    // Converts message to JSON format
    static class JsonLogger extends LoggerDecorator {

        JsonLogger(Logger logger) {
            super(logger);
        }

        @Override
        public void log(String message) {
            String json = "{ \"log\": \"" + message + "\" }";
            logger.log(json);
        }
    }

    // =======================
    // 5. Client
    // =======================
    public static void main(String[] args) {

        Logger logger = new ConsoleLogger();

        // Dynamically adding behaviors at runtime
        logger = new TimestampLogger(logger);
        logger = new LevelLogger(logger, "INFO");
        logger = new JsonLogger(logger);

        logger.log("User logged in");
    }
}