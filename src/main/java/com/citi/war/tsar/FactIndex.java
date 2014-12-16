package com.citi.war.tsar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author sovsyankin
 */
public class FactIndex {

    public Map<Map<String, String>, List<SystemDate>> indexByEntry(String fileName) {
        Map<Map<String, String>, List<SystemDate>> index = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.lines().forEach(rawLine -> {
                final LogEntry logEntry = ParseUtils.parseLogEntry(rawLine);
                index.computeIfAbsent(logEntry.getAttributes(), attrs -> new ArrayList<>())
                        .add(new SystemDate(logEntry.getSystem(), logEntry.getDate()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        index.entrySet().forEach(new Consumer<Map.Entry<Map<String, String>, List<SystemDate>>>() {
            @Override
            public void accept(Map.Entry<Map<String, String>, List<SystemDate>> entry) {
                Collections.sort(entry.getValue());
            }
        });

        return index;
    }
}
