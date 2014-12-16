package com.citi.war.tsar;

/**
 * @author dbrusentsov
 * @since 16.12.2014
 */
public class SystemDate implements Comparable<SystemDate> {

    public final String system;

    public final Long date;

    public SystemDate(String system, Long date) {
        this.system = system;
        this.date = date;
    }

    @Override
    public int compareTo(SystemDate o) {
        return date.compareTo(o.date);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SystemDate{");
        sb.append("system='").append(system).append('\'');
        sb.append(", date=").append(date);
        sb.append('}');
        return sb.toString();
    }
}
