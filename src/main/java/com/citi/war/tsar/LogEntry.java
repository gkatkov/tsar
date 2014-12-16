package com.citi.war.tsar;

import java.util.Date;
import java.util.Map;

/**
 * @author sovsyankin
 * @since 16.12.2014
 */
public class LogEntry {
    private final Date date;

    private final String system;

    private final Map<String, String> attributes;

    public LogEntry(Date date, String system, Map<String, String> attributes) {

        this.date = date;
        this.system = system;
        this.attributes = attributes;
    }

    public Date getDate() {
        return date;
    }

    public String getSystem() {
        return system;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogEntry logEntry = (LogEntry) o;

        if (attributes != null ? !attributes.equals(logEntry.attributes) : logEntry.attributes != null) return false;
        if (date != null ? !date.equals(logEntry.date) : logEntry.date != null) return false;
        if (system != null ? !system.equals(logEntry.system) : logEntry.system != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (system != null ? system.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }
}
