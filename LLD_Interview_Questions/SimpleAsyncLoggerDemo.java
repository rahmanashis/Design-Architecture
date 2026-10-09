/*

✅ Functional

	Multiple log levels

	Per-package logger

	Multiple appenders

	Pattern-based formatting

	Asynchronous logging


✅ Non-Functional

	High throughput

	Non-blocking

	Thread-safe

	Extensible

	Minimal lock contention



Client
   ↓
Logger (per class)
   ↓
AsyncLogger (Queue)
   ↓
LogDispatcher (Worker Thread)
   ↓
Appender(s)
   ↓
Formatter

*/



import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// ================= LOG LEVEL =================
enum LogLevel {
    DEBUG(1), INFO(2), WARN(3), ERROR(4);

    private int priority;

    LogLevel(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}

// ================= LOG MESSAGE =================
class LogMessage {
    private LocalDateTime timestamp;
    private LogLevel level;
    private String message;

    public LogMessage(LogLevel level, String message) {
        this.timestamp = LocalDateTime.now();
        this.level = level;
        this.message = message;
    }

    public String format() {
        return "[" + timestamp + "] [" + level + "] " + message;
    }

    public LogLevel getLevel() {
        return level;
    }
}

// ================= APPENDER STRATEGY =================
interface Appender {
    void append(LogMessage message);
}

// ================= CONSOLE APPENDER =================
class ConsoleAppender implements Appender {

    @Override
    public void append(LogMessage message) {
        System.out.println(message.format());
    }
}

// ================= FILE APPENDER =================
class FileAppender implements Appender {

    private String filePath;

    public FileAppender(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public synchronized void append(LogMessage message) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(message.format() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// ================= LOGGER CONFIG =================
class LoggerConfig {
    private LogLevel globalLevel;
    private List<Appender> appenders = new ArrayList<>();

    public LoggerConfig(LogLevel level) {
        this.globalLevel = level;
    }

    public void addAppender(Appender appender) {
        appenders.add(appender);
    }

    public LogLevel getGlobalLevel() {
        return globalLevel;
    }

    public List<Appender> getAppenders() {
        return appenders;
    }
}

// ================= LOGGER (ASYNC USING EXECUTOR) =================
class Logger {

    private static Logger instance;
    private LoggerConfig config;
    private ExecutorService executor;

    private Logger(LoggerConfig config) {
        this.config = config;
        this.executor = Executors.newFixedThreadPool(2); // async workers
    }

    public static synchronized Logger getInstance(LoggerConfig config) {
        if (instance == null) {
            instance = new Logger(config);
        }
        return instance;
    }

    public void log(LogLevel level, String message) {
        if (level.getPriority() >= config.getGlobalLevel().getPriority()) {

            LogMessage logMessage = new LogMessage(level, message);

            for (Appender appender : config.getAppenders()) {
                executor.submit(() -> appender.append(logMessage));
            }
        }
    }

    public void debug(String msg) { log(LogLevel.DEBUG, msg); }
    public void info(String msg)  { log(LogLevel.INFO, msg); }
    public void warn(String msg)  { log(LogLevel.WARN, msg); }
    public void error(String msg) { log(LogLevel.ERROR, msg); }

    public void shutdown() {
        executor.shutdown();
    }
}

// ================= DEMO =================
public class SimpleAsyncLoggerDemo {

    public static void main(String[] args) {

        LoggerConfig config = new LoggerConfig(LogLevel.INFO);
        config.addAppender(new ConsoleAppender());
        config.addAppender(new FileAppender("app.log"));

        Logger logger = Logger.getInstance(config);

        logger.debug("This won't print");
        logger.info("Application started");
        logger.error("Something failed!");

        logger.shutdown();
    }
}