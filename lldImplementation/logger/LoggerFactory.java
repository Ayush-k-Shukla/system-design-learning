package logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LoggerFactory {
    private static final ConcurrentMap<String, Logger> loggers = new ConcurrentHashMap<>();

    public static Logger getLogger(String name) {
        return loggers.computeIfAbsent(name, n -> new Logger());
    }
}
