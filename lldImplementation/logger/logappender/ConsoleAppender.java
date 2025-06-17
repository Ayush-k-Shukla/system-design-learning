package logger.logappender;

import logger.LogMessage;
import logger.logformatter.LogFormatter;

public class ConsoleAppender implements LogAppender {
    private final LogFormatter formatter;

    public ConsoleAppender(LogFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public synchronized void append(LogMessage logMessage) {
        System.out.println(formatter.format(logMessage));
    }
}
