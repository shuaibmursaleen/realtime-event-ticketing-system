package com.shuaib.oopcw.eventstreams;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class LogStream {
    private static LogStream instance;
    private final Logger logger;
    private Sinks.Many<String> logsSink;

    private LogStream() {
        this.logger = LogManager.getLogger("GLOBAL");
        this.logsSink = Sinks.many().replay().limit(1);
    }

    public static LogStream getInstance() {
        if (instance == null) {
            instance = new LogStream();
        }
        return instance;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public void addEvent(String log) {
        logsSink.tryEmitNext(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) + log);
        logger.info(log);
        System.out.println();
    }

    public Flux<String> getStream() {
        return this.logsSink.asFlux()
                .doOnCancel(() -> logger.info("Client disconnected from log stream"))
                .doOnError(error -> logger.error("Error in log stream: {}", error.getMessage()));
    }
}
