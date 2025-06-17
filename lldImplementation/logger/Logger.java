package logger;

import logger.logappender.ConsoleAppender;
import logger.logformatter.DefaultFormatter;

public class Logger {
    private volatile LoggerConfig config;

    public Logger() {
        config = new LoggerConfig(LogLevel.INFO, new ConsoleAppender(new DefaultFormatter()));
    }

    public Logger(LoggerConfig config) {
        this.config = config;
    }

    public void setConfig(LoggerConfig config) {
        this.config = config;
    }

    public void log(LogLevel level, String message) {
        LoggerConfig currentConfig = this.config;
        // if current log level has higher order then only log
        if (level.ordinal() >= currentConfig.getLogLevel().ordinal()) {
            LogMessage msg = new LogMessage(level, message);
            currentConfig.getLogAppender().append(msg);
        }
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warning(String message) {
        log(LogLevel.WARN, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void fatal(String message) {
        log(LogLevel.FATAL, message);
    }
}
