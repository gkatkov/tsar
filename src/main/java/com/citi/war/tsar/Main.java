package com.citi.war.tsar;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.System.lineSeparator;

/**
 * @author sovsyankin
 * @since 16.12.2014
 */
public class Main {

    public static void main(String[] args) {
        checkArgument(args.length == 2, "please provide output log and path to store dot");
        final String fileName = args[0];
        final String dotFileName = args[1];
        final FactIndexer factIndexer = new FactIndexer();
        final Index index = factIndexer.indexByEntry(fileName);
        final Map<Map<String, String>, List<SystemDate>> paths = index.paths();
        final TerminatingSystemResolver terminatingSystemResolver = new TerminatingSystemResolver();
        terminatingSystemResolver.resolve(index);
        System.out.println("# of facts total: " + paths.size());
        System.out.println("# of simple paths: " + index.getSimple().size());
        System.out.println("# of complex paths: " + index.getComplex().size());
        final TreeBuilder treeBuilder = new TreeBuilder();
        final TreeStats treeStats = treeBuilder.buildTree(paths);
        System.out.println("Entry nodes ************");
        treeStats.getOriginators().forEach(System.out::println);
        System.out.println("Terminal nodes *********");
        treeStats.getTerminals().forEach(System.out::println);

        final GraphBuilder graphBuilder = new GraphBuilder(index, treeStats.getOriginators(), treeStats.getTerminals());
        final Collection<GraphBuilder.Edge> edges = graphBuilder.construct();
        edges.forEach(System.out::println);
        final Optional<String> dotFragment = edges.stream().map(edge -> edge.getFrom() + " -> " + edge.getTo() + ";" + lineSeparator()).reduce((s, s2) -> s + s2);
        try (FileWriter fos = new FileWriter(dotFileName)) {
            fos.write("digraph G {");
            fos.write(dotFragment.get());
            fos.write("}");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Map<String, List<Map<String, String>>> path2Facts = paths.entrySet().stream().collect(Collectors.toMap(new Function<Map.Entry<Map<String, String>, List<SystemDate>>, String>() {
//            @Override
//            public String apply(Map.Entry<Map<String, String>, List<SystemDate>> t) {
//                return t.getValue().stream().map(SystemDate::getSystem).collect(Collectors.joining("->"));
//            }
//        }, new Function<Map.Entry<Map<String, String>, List<SystemDate>>, List<Map<String, String>>>() {
//            @Override
//            public List<Map<String, String>> apply(Map.Entry<Map<String, String>, List<SystemDate>> t) {
//                ArrayList<Map<String, String>> maps = new ArrayList<>();
//                maps.add(t.getKey());
//                return maps;
//            }
//        }, new BinaryOperator<List<Map<String, String>>>() {
//            @Override
//            public List<Map<String, String>> apply(List<Map<String, String>> o, List<Map<String, String>> o2) {
//                o.addAll(o2);
//                return o;
//            }
//        }));
//        try {
//            PrintStream ps = new PrintStream("C:/path2facts2");
//
//            path2Facts.forEach(new BiConsumer<String, List<Map<String, String>>>() {
//                @Override
//                public void accept(String s, List<Map<String, String>> maps) {
//                    ps.println(s);
//                    maps.forEach(ps::println);
//                }
//            });
//
//            ps.flush();
//            ps.close();
//
//            PrintStream doubles = new PrintStream("C:/path2factsDoubles2");
//
//            path2Facts.forEach(new BiConsumer<String, List<Map<String, String>>>() {
//                @Override
//                public void accept(String s, List<Map<String, String>> maps) {
//                    if(maps.size() > 1){
//                        doubles.println(s);
//                        maps.forEach(doubles::println);
//                    }
//                }
//            });
//
//            doubles.flush();
//            doubles.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

//        final Set<String> probablyTerminatingSystems = terminatingSystemResolver.resolve(index);
//
//        final List<Pair<Map<String, String>, Set<String>>> fact2ProbablyFailedSystem = paths.entrySet()
//                .stream()
//                .map(entry -> Pair.with(entry.getKey(), Iterables.getLast(entry.getValue()).system))
//                .filter(pair -> probablyTerminatingSystems.contains(pair.getValue1()))
//                .map(new Function<Pair<Map<String, String>, String>, Pair<Map<String, String>, Set<String>>>() {
//                    @Override
//                    public Pair<Map<String, String>, Set<String>> apply(Pair<Map<String, String>, String> objects) {
//                        final Set<String> nextPossibleSystems = treeStats
//                                .getTree()
//                                .getOrDefault(objects.getValue1(), Collections.<String>emptySet());
//                        final Set<String> probablyFailedSystems = new HashSet<>(nextPossibleSystems);
//                        probablyFailedSystems.removeAll(probablyTerminatingSystems);
//                        probablyFailedSystems.removeAll(treeStats.getOriginators());
//                        return Pair.with(objects.getValue0(), probablyFailedSystems);
//                    }
//                })
//                .collect(Collectors.toList());
//
//        int sum = fact2ProbablyFailedSystem.stream().map(Pair::getValue1).mapToInt(Set::size).sum();
//        System.out.println("total possible failed systems: " + sum);

        new GraphExporter().exportGraph(treeStats);
    }
}
