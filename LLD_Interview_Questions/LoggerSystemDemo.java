/*
LLD Of Logger System

Functional Requirements
	Support multiple log levels:
		DEBUG

		INFO

		WARN

		ERROR

	Log message should include:

		timestamp

		level

		message

	Support multiple output destinations{Appenders}:

		Console

		File

		(extensible → DB, Kafka, etc.)

	Should support setting global log level


Non-Functional Requirements

	Thread-safe

	Extensible

	Low latency

	Open for adding new appenders


Step 2: Identify Core Components
	Logger
	LogLevel (enum)
	LogMessage
	Appender (interface)
	   - ConsoleAppender
	   - FileAppender
	LoggerConfig


Why LoggerConfig?

	To configure:

	Log level

	Appenders list

	Keeps Logger clean.


Client
   ↓
Logger
   ↓
Appender (Strategy Pattern)

Strategy Pattern → Appenders

Singleton Pattern → Logger

Open/Closed Principle → New appender without modifying logger

*/

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// -------- ENUM --------
enum LogLevel {
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4);

    private int priority;

    LogLevel(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}

// -------- MODEL --------
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

// -------- STRATEGY --------
interface Appender {
    void append(LogMessage message);
}

// -------- CONSOLE APPENDER --------
class ConsoleAppender implements Appender {
    @Override
    public void append(LogMessage message) {
        System.out.println(message.format());
    }
}

// -------- FILE APPENDER --------
class FileAppender implements Appender {

    private String filePath;

    public FileAppender(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void append(LogMessage message) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(message.format() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// -------- CONFIG --------
class LoggerConfig {
    private LogLevel globalLevel;
    private List<Appender> appenders = new ArrayList<>();

    public LoggerConfig(LogLevel globalLevel) {
        this.globalLevel = globalLevel;
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

// -------- SINGLETON LOGGER --------
class Logger {

    private static Logger instance;
    private LoggerConfig config;

    private Logger(LoggerConfig config) {
        this.config = config;
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
                appender.append(logMessage);
            }
        }
    }

    public void debug(String msg) {
        log(LogLevel.DEBUG, msg);
    }

    public void info(String msg) {
        log(LogLevel.INFO, msg);
    }

    public void warn(String msg) {
        log(LogLevel.WARN, msg);
    }

    public void error(String msg) {
        log(LogLevel.ERROR, msg);
    }
}

// -------- CLIENT --------
public class LoggerSystemDemo {

    public static void main(String[] args) {

        LoggerConfig config = new LoggerConfig(LogLevel.INFO);
        config.addAppender(new ConsoleAppender());
        config.addAppender(new FileAppender("app.log"));

        Logger logger = Logger.getInstance(config);

        logger.debug("This is debug message"); // won't print
        logger.info("Application started");
        logger.error("Something failed!");
    }
}