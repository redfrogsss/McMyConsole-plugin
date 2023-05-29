package dev.redfrogss.mcmyconsole.classes;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LogAppender extends AbstractAppender {

    // your variables
    SimpleDateFormat formatter;

    ArrayList<String> consoleLogs;

    public LogAppender(ArrayList<String> consoleLogs) {
        // do your calculations here before starting to capture
        super("MyLogAppender", null, null, true, null);
        start();

        formatter = new SimpleDateFormat("HH:mm:ss");

        this.consoleLogs = consoleLogs;
    }

    @Override
    public void append(LogEvent event) {
        // if you don`t make it immutable, then you may have some unexpected behaviours
        LogEvent log = event.toImmutable();

        // do what you have to do with the log

        // you can get only the log message like this:
        String message = log.getMessage().getFormattedMessage();

        // and you can construct your whole log message like this:
        message = "[" + formatter.format(new Date(event.getTimeMillis())) + " " + event.getLevel().toString() + "] " + message;

        consoleLogs.add(message);
    }

}