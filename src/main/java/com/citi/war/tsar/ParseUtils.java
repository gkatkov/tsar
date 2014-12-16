package com.citi.war.tsar;

import java.util.Date;
import java.util.Map;
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
}
