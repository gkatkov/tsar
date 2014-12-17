package com.citi.war.tsar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Sets.newHashSet;
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
        new GraphExporter().exportGraph(treeStats);
    }
}
