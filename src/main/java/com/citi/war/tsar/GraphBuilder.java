package com.citi.war.tsar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.function.Function.identity;

/**
 * @author sovsyankin
 * @since 17.12.2014
 */
public class GraphBuilder {

    private final Index index;

    private final Set<String> originators;

    private final Set<String> terminals;

    public GraphBuilder(Index index, Set<String> originators, Set<String> terminals) {
        this.index = index;
        this.originators = originators;
        this.terminals = terminals;
    }

    public Collection<Edge> construct() {
        final Map<Map<String, String>, List<SystemDate>> paths = index.paths();
        final Map<Edge, Edge> edges = new HashMap<>();
        for (Map<String, String> fact : index.getSimple()) {
            final List<SystemDate> path = paths.get(fact);
//            path.stream().map(SystemDate::getSystem).forEach(s -> System.out.print(s + ", "));
//            System.out.println();
            if (path.size() <= 1) {
                System.out.println("incorrect path (a path must have at least two nodes): " + fact + ", path: " + path);
                continue;
            }
            final ArrayList<Edge> branch = toBranch(path);
            for (int i = 0; i < branch.size(); i++) {
                Edge edge = branch.get(i);
                if (isTerminal(edge.from)) {
                    // backtrack to first non terminal and make an edge <nonterminal>-edge.to
                    final Edge firstNonTerminatingEdge = findBackward(branch, i, e -> !isTerminal(e.getTo()));
                    if (firstNonTerminatingEdge == null) {
                        //fixme handle
                        System.out.println("found no non-terminals");
                        continue;
                    }
                    edge = new Edge(firstNonTerminatingEdge.to, edge.to);
                }
                edges.computeIfAbsent(edge, identity()).inc();
            }
        }

        for (Map<String, String> fact : index.getComplex()) {

        }

        final Set<Edge> e = edges.keySet();
        cleanup(e);
        return e;
    }

    private void cleanup(Set<Edge> edges) {
        newHashSet(edges).stream().filter(e -> e.getWeight() < 20).forEach(edges::remove);
    }

    private Edge findBackward(ArrayList<Edge> branch, int startFrom, Predicate<Edge> condition) {
        if (startFrom == 0) {
            return null;
        }
        for (int i = startFrom - 1; i >= 0; i--) {
            final Edge edge = branch.get(i);
            if (condition.test(edge)) {
                return edge;
            }
        }
        return null;
    }

    private ArrayList<Edge> toBranch(List<SystemDate> path) {
        ArrayList<Edge> branch = new ArrayList<>(path.size() - 1);
        for (int i = 0; i < path.size() - 1; i++) {
            branch.add(toEdge(path, i));
        }
        return branch;
    }

    private boolean isTerminal(String node) {
        return terminals.contains(node);
    }

    private Edge toEdge(List<SystemDate> path, int edgeIndex) {
        return new Edge(path.get(edgeIndex).system, path.get(edgeIndex + 1).system);
    }

    public static class Edge {
        private final String from;

        private final String to;

        private int weight;

        public Edge(String from, String to) {
            this.from = from;
            this.to = to;
        }

        protected void inc() {
            weight++;
        }

        private void dec() {
            weight++;
        }

        private void adjust(int i) {
            weight += i;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge edge = (Edge) o;

            if (from != null ? !from.equals(edge.from) : edge.from != null) return false;
            if (to != null ? !to.equals(edge.to) : edge.to != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = from != null ? from.hashCode() : 0;
            result = 31 * result + (to != null ? to.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "edge[from=" + from + ", to=" + to + ", weight=" + weight + "]";
        }
    }
}
