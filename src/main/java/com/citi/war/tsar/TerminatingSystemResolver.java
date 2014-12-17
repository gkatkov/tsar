package com.citi.war.tsar;

import org.javatuples.Triplet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.firstNonNull;
import static org.javatuples.Triplet.with;

/**
 * @author dbrusentsov
 * @since 17.12.2014
 */
public class TerminatingSystemResolver {

    private static final float IGNORE_LESS = 0.015F;

    public Set<String> resolve(Index index) {
        final Set<String> firsts = new HashSet<>();
        final Map<String, Integer> name2Last = new HashMap<>();

        index.paths().values().forEach(flow -> {
            firsts.add(flow.get(0).system);
            name2Last.compute(flow.get(flow.size() - 1).system, (sys, cnt) -> firstNonNull(cnt, 0) + 1);
        });

        firsts.forEach(name2Last::remove);

        final int totalCount = name2Last
                .values()
                .stream()
                .mapToInt(v -> v)
                .sum();

        final List<Triplet<String, Integer, Float>> systemCountWeights = name2Last
                .entrySet()
                .stream()
                .map(sysCntEntry -> with(sysCntEntry.getKey(), sysCntEntry.getValue(), (float) sysCntEntry.getValue() / totalCount))
                .sorted((o1, o2) -> o1.getValue1().compareTo(o2.getValue1()))
                .collect(Collectors.toList());

        final Set<String> result = new HashSet<>();

        System.out.println("**********************");
        System.out.println("*terminating systems**");
        System.out.println("**********************");

        float totalWeight = 0;
        for (Triplet<String, Integer, Float> systemCountWeight : systemCountWeights) {
            totalWeight += systemCountWeight.getValue2();
            if (totalWeight < IGNORE_LESS) {
                System.out.println("failed: " + systemCountWeight);
                continue;
            }
            System.out.println("terminating: " + systemCountWeight);
            result.add(systemCountWeight.getValue0());
        }

        System.out.println("***********************");

        return result;
    }
}