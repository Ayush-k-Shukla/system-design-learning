package logger;

import logger.logappender.ConsoleAppender;
import logger.logformatter.DefaultFormatter;

public class Logger {
    // Eager initialization Singleton
    private static final Logger instance = new Logger();
    private LoggerConfig config;

    // Private Constructor for singleton
    private Logger(){
        config = new LoggerConfig(LogLevel.INFO,new ConsoleAppender(new DefaultFormatter()));
    }

    public static Logger getInstance(){
        return instance;
    }

    public void setConfig(LoggerConfig config){
        this.config = config;
    }

    public void log(LogLevel level,String message){
        // if current log level has higher order then only log
        if(level.ordinal() >= config.getLogLevel().ordinal()){
            LogMessage msg = new LogMessage(level,message);
            config.getLogAppender().append(msg);
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
