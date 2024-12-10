package com.shuaib.oopcw.logs;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class LogsHelper {
    private static LogsHelper instance; 
    private final Logger logger;
    private Sinks.Many<String> logsSink;
    
    private LogsHelper() {
    this.logger = LogManager.getLogger("GLOBAL");
    this.logsSink = Sinks.many().replay().limit(1);
    }

    public static LogsHelper getInstance() {
        if (instance == null) {
            instance = new LogsHelper();
        }
        return instance;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public synchronized void addLog(String log) {
        logsSink.tryEmitNext(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date())+ log);
        logger.info(log);
        System.out.println();
    }

    public Flux<String> getLogsSink() {
        return this.logsSink.asFlux();
    }
}
