package logger.logformatter;

import logger.LogMessage;

public interface LogFormatter {
    String format(LogMessage message);
}
