package me.dev.faceless.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Timings {
    private static final Logger Logger = LoggerFactory.getLogger(Timings.class);

    public static void runWithTiming(String operationName, Runnable task) {
        long startTime = System.currentTimeMillis();
        try {
            task.run();
        } finally {
            Logger.info("{} completed in {}ms", operationName, System.currentTimeMillis() - startTime);
        }
    }
}
