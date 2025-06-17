package logger.logappender;

import logger.LogMessage;
import logger.logformatter.LogFormatter;

import java.io.FileWriter;
import java.io.IOException;

public class FileAppender implements LogAppender {
    private FileWriter writer;
    private final LogFormatter formatter;

    public FileAppender(String filePath,LogFormatter formatter) {
        this.formatter = formatter;
        try {
            this.writer = new FileWriter(filePath,true);
        }
        catch (Exception e){
            System.out.println("Failed to create log file: " + e.getMessage());
        }
    }

    @Override
    public synchronized void append(LogMessage logMessage) {
        try {
            writer.write(formatter.format(logMessage) + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to write log: " + e.getMessage());
        }
    }
}
