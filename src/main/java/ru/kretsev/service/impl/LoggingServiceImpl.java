package ru.kretsev.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.slf4j.spi.LoggingEventBuilder;
import org.springframework.stereotype.Service;
import ru.kretsev.service.LoggingService;

@Slf4j
@Service
public class LoggingServiceImpl implements LoggingService {
    @Override
    public void logDebug(String message, Object... args) {
        logMessage(Level.DEBUG, message, args);
    }

    @Override
    public void logInfo(String message, Object... args) {
        logMessage(Level.INFO, message, args);
    }

    @Override
    public void logWarn(String message, Object... args) {
        logMessage(Level.WARN, message, args);
    }

    @Override
    public void logError(String message, Object... args) {
        logMessage(Level.ERROR, message, args);
    }

    private void logMessage(Level level, String message, Object... args) {
        LoggingEventBuilder logBuilder;

        switch (level) {
            case DEBUG -> logBuilder = log.atDebug();
            case ERROR -> logBuilder = log.atError();
            case WARN -> logBuilder = log.atWarn();
            case INFO -> logBuilder = log.atInfo();
            default -> throw new IllegalArgumentException("Unsupported log level: " + level);
        }

        logBuilder = logBuilder.setMessage(message);

        for (Object arg : args) {
            logBuilder = logBuilder.addArgument(() -> arg);
        }

        logBuilder.log();
    }
}
