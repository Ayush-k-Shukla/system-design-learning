package logger;

import logger.logappender.ConsoleAppender;
import logger.logappender.FileAppender;
import logger.logformatter.DefaultFormatter;

public class LoggerDemo {
    public static void run() {
        // Logger with default config (INFO, Console)
        Logger loggerA = LoggerFactory.getLogger("A");
        loggerA.info("[A] Info log");
        loggerA.debug("[A] Debug log");
        loggerA.warning("[A] Warning log");
        loggerA.error("[A] Error log");
        loggerA.fatal("[A] Fatal log");

        // Logger with custom config (DEBUG, File)
        Logger loggerB = LoggerFactory.getLogger("B");
        LoggerConfig configB = new LoggerConfig(LogLevel.DEBUG, new FileAppender("app.log", new DefaultFormatter()));
        loggerB.setConfig(configB);
        loggerB.info("[B] Info log");
        loggerB.debug("[B] Debug log");
        loggerB.warning("[B] Warning log");
        loggerB.error("[B] Error log");
        loggerB.fatal("[B] Fatal log");

        // Logger with custom config (ERROR, Console)
        Logger loggerC = LoggerFactory.getLogger("C");
        LoggerConfig configC = new LoggerConfig(LogLevel.ERROR, new ConsoleAppender(new DefaultFormatter()));
        loggerC.setConfig(configC);
        loggerC.info("[C] Info log (should not appear)");
        loggerC.debug("[C] Debug log (should not appear)");
        loggerC.warning("[C] Warning log (should not appear)");
        loggerC.error("[C] Error log");
        loggerC.fatal("[C] Fatal log");

        // Test thread safety: log from multiple threads
        Runnable logTask = () -> {
            Logger threadLogger = LoggerFactory.getLogger("ThreadLogger");
            for (int i = 0; i < 5; i++) {
                threadLogger.info(Thread.currentThread().getName() + " - info " + i);
            }
        };
        Thread t1 = new Thread(logTask, "T1");
        Thread t2 = new Thread(logTask, "T2");
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
