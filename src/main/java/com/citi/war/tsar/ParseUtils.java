package com.citi.war.tsar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * @author sovsyankin
 * @since 16.12.2014
 */
public class ParseUtils {
    public static LogEntry parseLogEntry(String raw) {
        final String[] parts = raw.split(" ", 4);
        final Date date = new Date(Long.valueOf(parts[0]));
        final String system = parts[1];
        final String factString = parts[3];
        final String rawAttributes = factString.substring(factString.indexOf("[") + 1, factString.indexOf("]"));
        final String[] splitAttributes = rawAttributes.split(",");
        final Map<String, String> attributes = Stream.of(splitAttributes)
                .map(s -> s.split(":"))
                .collect(toMap(sa -> sa[0].trim(), sa -> sa[1].trim()));
        return new LogEntry(date, system, attributes);
    }

    public static Map<Map<String, String>, List<String>> collectPaths(Supplier<LogEntry> logEntrySupplier) {
        Map<Map<String, String>, List<String>> collector = new HashMap<>();
        LogEntry logEntry;
        while ((logEntry = logEntrySupplier.get()) != null) {
            collector.computeIfAbsent(logEntry.getAttributes(), i -> new ArrayList<>()).add(logEntry.getSystem());
        }
        System.out.println(collector.size() + " facts are found");
        return collector;
    }
}
