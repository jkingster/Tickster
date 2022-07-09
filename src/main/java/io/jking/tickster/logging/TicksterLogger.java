package io.jking.tickster.logging;

import io.jking.tickster.core.Tickster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TicksterLogger {
    private static final Logger logger = LoggerFactory.getLogger(TicksterLogger.class);

    private TicksterLogger() {
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void warn(LogType logType, Object... objects) {
        logger.warn(logType.getLogMessage().formatted(objects));
    }

    public static void log(LogType logType, Object... objects) {
        logger.info(logType.getLogMessage().formatted(objects));
    }

}
