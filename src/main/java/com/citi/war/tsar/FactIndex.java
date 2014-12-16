package com.citi.war.tsar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author sovsyankin
 */
public class FactIndex {

    public Map<Map<String, String>, List<SystemDate>> indexByEntry(String fileName) {
        Map<Map<String, String>, List<SystemDate>> index = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//            final AtomicInteger i = new AtomicInteger(0);
            reader.lines().forEach(rawLine -> {
                final LogEntry logEntry = ParseUtils.parseLogEntry(rawLine);
//                if (i.incrementAndGet() % 1000 == 0) {
//                    System.out.println(i);
//                }
                index.computeIfAbsent(logEntry.getAttributes(), attrs -> new ArrayList<>())
                        .add(new SystemDate(logEntry.getSystem(), logEntry.getDate()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        index.entrySet().forEach(new Consumer<Map.Entry<Map<String, String>, List<SystemDate>>>() {
            @Override
            public void accept(Map.Entry<Map<String, String>, List<SystemDate>> entry) {
                List<SystemDate> path = entry.getValue();

                Collections.sort(path);

                if(path.get(0).system.equals("L_1_Sys_2")){
                    path.forEach(new Consumer<SystemDate>() {
                        @Override
                        public void accept(SystemDate systemDate) {
                            System.out.print(systemDate.system + ", ");
                        }
                    });
                    System.out.println();
                }

                if(Objects.equals(entry.getKey().get("atzRH"), "478947")){
                    path.forEach(new Consumer<SystemDate>() {
                        @Override
                        public void accept(SystemDate systemDate) {
                            System.out.println(systemDate);
                        }
                    });
                }
            }
        });

        return index;
    }

    public static void main(String[] args) {
        final FactIndex factIndex = new FactIndex();
        final Map<Map<String, String>, List<SystemDate>> mapListMap = factIndex.indexByEntry("C:\\Users\\dbrusentsov\\Downloads\\output-sample\\output-sample.log");
        for (Map.Entry<Map<String, String>, List<SystemDate>> mapListEntry : mapListMap.entrySet()) {
            System.out.println(mapListEntry.getKey() + " " + mapListEntry.getValue());
        }
        System.out.println("# of facts: " + mapListMap.size());
    }
}
