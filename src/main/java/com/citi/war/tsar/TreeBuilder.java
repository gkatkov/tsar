package com.citi.war.tsar;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Created by GKatkov on 16.12.2014.
 */
public class TreeBuilder {

    public Map<String, Set<String>> buildTree(List<List<String>> paths) {
        Map<String, AtomicLong> id2Finish = new HashMap<>();
        
        HashMap<String, Set<String>> tree = new HashMap<>();
        for (List<String> path : paths) {
            for (int i = 0; i < path.size(); i++) {
                String nodeId = path.get(i);
                if (i == 0) {
                    tree.computeIfAbsent(null, n -> new HashSet<>()).add(nodeId);
                } else {
                    tree.computeIfAbsent(path.get(i - 1), n -> new HashSet<>()).add(nodeId);
                }
                if (i == path.size() - 1) {
                    id2Finish.computeIfAbsent(nodeId, id -> new AtomicLong(0)).incrementAndGet();
                }
            }
        }
        List<Map.Entry<String, AtomicLong>> sortedTerminateNodes = id2Finish.entrySet().stream().sorted(new Comparator<Map.Entry<String, AtomicLong>>() {
            @Override
            public int compare(Map.Entry<String, AtomicLong> o1, Map.Entry<String, AtomicLong> o2) {
                return new Long(o1.getValue().get()).compareTo(o2.getValue().get());
            }
        }).collect(Collectors.toList());
        sortedTerminateNodes.forEach(System.out::println);
        System.out.println("*******************************************");
        System.out.println("Originating systems: " + tree.get(null));
        return tree;
    }
}
