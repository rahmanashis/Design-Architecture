
/*
Logging System

Desc:
	1. to track the flow of the codeing paths
	2. to debug if any issue occour
	3. to prove things/auditing


Functional Requirements:
1. client should be able to log the messages
	timestamp
	level
	message
	logSource

2. Support multiple log levels:
	Debug -1 
	INFO -2
	WARN -3
	ERROR
	FATAL

3. Multiple output Appenders: Destination
	Console
	File
	DB, Kafka

4. Should support setting a global log level


Non-Functional Requirements:
 - thread-safe
 - Extensible
 		- open for adding new appenders


Core components:
	Logger
	LogLevel(enum)
	LogMessage
	Appender (interface)
		- FileAppender
		- ConsoleAppender
	Formatter (interface) – optional

Client --> Logger --> Appenders 

Strategy Pattern --> Appenders
Singleton Pattern --> Logger

Core design and relationships described above.
*/

public class Logger_class_code {
    public static void main(String[] args) {
        System.out.println("Logger design notes. See LoggerSystemDemo.java or SimpleAsyncLoggerDemo.java for full implementation.");
    }
}