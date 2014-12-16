package com.citi.war.tsar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sovsyankin
 */
public class FactIndex {

    public Map<Map<String, String>, List<String>> indexByEntry(String fileName) {
        Map<Map<String, String>, List<String>> index = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            final AtomicInteger i = new AtomicInteger(0);
            reader.lines().forEach(rawLine -> {
                final LogEntry logEntry = ParseUtils.parseLogEntry(rawLine);
                if (i.incrementAndGet() % 1000 == 0) {
                    System.out.println(i);
                }
                List<String> systems = index.get(logEntry.getAttributes());
                if (systems == null) {
                    index.put(logEntry.getAttributes(), systems = new ArrayList<>());
                }
                systems.add(logEntry.getSystem());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return index;
    }

    public static void main(String[] args) {
        final FactIndex factIndex = new FactIndex();
        final Map<Map<String, String>, List<String>> mapListMap = factIndex.indexByEntry("c:/users/sovsyankin/war/output-sample.log");
        for (Map.Entry<Map<String, String>, List<String>> mapListEntry : mapListMap.entrySet()) {
            System.out.println(mapListEntry.getKey() + " " + mapListEntry.getValue());
        }
        System.out.println("# of facts: " + mapListMap.size());
    }
}
