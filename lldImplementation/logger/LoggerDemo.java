package logger;

import logger.logappender.FileAppender;
import logger.logformatter.DefaultFormatter;

public class LoggerDemo {
    public static void run(){
        Logger logger = Logger.getInstance();

        // Default config with level info
        logger.info("Info log");
        logger.debug("Debug log");
        logger.warning("Warning log");
        logger.error("Error log");
        logger.fatal("Fatal log");

        // With the lowest level Debug
        LoggerConfig newConfig =new LoggerConfig(LogLevel.DEBUG,new FileAppender("app.log",new DefaultFormatter()));
        logger.setConfig(newConfig);
        logger.info("Info log");
        logger.debug("Debug log");
        logger.warning("Warning log");
        logger.error("Error log");
        logger.fatal("Fatal log");

    }
}
