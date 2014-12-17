package com.citi.war.tsar;

import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.collect.Iterables.getLast;
import static org.javatuples.Triplet.with;

/**
 * @author dbrusentsov
 * @since 17.12.2014
 */
public class TerminatingSystemResolver {

    private static final float IGNORE_LESS = 0.001F;

    public Set<String> resolve(Index index) {
        final Set<String> firsts = new HashSet<>();
        final Map<String, Integer> last2Count = new HashMap<>();
        final Map<String, List<Map<String, String>>> last2Facts = new HashMap<>();

        index.paths().entrySet().forEach(factPath -> {
            final Map<String, String> fact = factPath.getKey();
            final List<SystemDate> flow = factPath.getValue();
            firsts.add(flow.get(0).system);
            final String system = getLast(flow).system;
            last2Count.compute(system, (sys, cnt) -> firstNonNull(cnt, 0) + 1);
            last2Facts.computeIfAbsent(system, s -> new ArrayList<>()).add(fact);
        });

        firsts.forEach(last2Count::remove);

        final long totalCount = last2Count
                .values()
                .stream()
                .collect(Collectors.summarizingInt(Number::intValue)).getSum();

        final List<Triplet<String, Integer, Float>> systemCountWeights = last2Count
                .entrySet()
                .stream()
                .map(sysCntEntry -> with(sysCntEntry.getKey(), sysCntEntry.getValue(), (float) sysCntEntry.getValue() / totalCount))
                .sorted((o1, o2) -> o1.getValue1().compareTo(o2.getValue1()))
                .collect(Collectors.toList());

        final Set<String> successes = new HashSet<>();
        final Set<String> fails = new HashSet<>();

        System.out.println("**********************");
        System.out.println("*terminating systems**");
        System.out.println("**********************");

        float totalWeight = 0;
        for (Triplet<String, Integer, Float> systemCountWeight : systemCountWeights) {
            totalWeight += systemCountWeight.getValue2();
            if (totalWeight < IGNORE_LESS) {
                System.out.println("failed: " + systemCountWeight);
                fails.add(systemCountWeight.getValue0());
            } else {
                System.out.println("terminating: " + systemCountWeight);
                successes.add(systemCountWeight.getValue0());
            }
        }

        System.out.println("***********************");
        System.out.println("******failed facts*****");
        System.out.println("***********************");

        fails.forEach(new Consumer<String>() {
            @Override
            public void accept(String system) {
                last2Facts.get(system).forEach(System.out::println);
            }
        });

        return successes;
    }
}