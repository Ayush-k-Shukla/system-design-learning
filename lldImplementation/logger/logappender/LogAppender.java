package logger.logappender;

import logger.LogMessage;

public interface LogAppender {
    void append(LogMessage logMessage);
}
