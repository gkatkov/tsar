package com.citi.war.tsar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author sovsyankin
 */
public class FactIndexer {

    public Index indexByEntry(String fileName) {
        Map<Map<String, String>, List<SystemDate>> paths = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.lines().forEach(rawLine -> {
                final LogEntry logEntry = ParseUtils.parseLogEntry(rawLine);
                paths.computeIfAbsent(logEntry.getAttributes(), attrs -> new ArrayList<>())
                        .add(new SystemDate(logEntry.getSystem(), logEntry.getDate()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Set<Map<String, String>> simple = new HashSet<>();
        final Set<Map<String, String>> complex = new HashSet<>();

        paths.entrySet().forEach(new Consumer<Map.Entry<Map<String, String>, List<SystemDate>>>() {
            @Override
            public void accept(Map.Entry<Map<String, String>, List<SystemDate>> entry) {
                Collections.sort(entry.getValue());
                if (isSimple(entry.getValue())) {
                    simple.add(entry.getKey());
                } else {
                    complex.add(entry.getKey());
                }
            }
        });

        return new Index(paths, simple, complex);
    }

    private boolean isSimple(List<SystemDate> path) {
        final Set<String> systems = new HashSet<>();
        for (SystemDate systemDate : path) {
            if (systems.contains(systemDate.system)) {
                return false;
            }
            systems.add(systemDate.system);
        }
        return true;
    }
}
