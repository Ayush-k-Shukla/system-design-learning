package logger.logformatter;

import logger.LogMessage;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DefaultFormatter implements LogFormatter {
    @Override
    public String format(LogMessage msg) {

        LocalDateTime dateTime = Instant.ofEpochMilli(msg.getTimestamp())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        String formatted = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return String.format("[%s] [%s] %s",
                formatted, msg.getLevel(), msg.getMessage());
    }
}
