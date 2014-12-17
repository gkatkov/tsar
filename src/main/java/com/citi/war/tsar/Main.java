package com.citi.war.tsar;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author sovsyankin
 * @since 16.12.2014
 */
public class Main {

    public static void main(String[] args) {
        checkArgument(args.length == 1, "file name required");
        final String fileName = args[0];
        final FactIndexer factIndexer = new FactIndexer();
        final Index index = factIndexer.indexByEntry(fileName);
        final Map<Map<String, String>, List<SystemDate>> paths = index.paths();
        System.out.println("# of facts total: " + paths.size());
        System.out.println("# of simple paths: " + index.getSimple().size());
        System.out.println("# of comples paths: " + index.getComplex().size());
        final TreeBuilder treeBuilder = new TreeBuilder();
        final TreeStats treeStats = treeBuilder.buildTree(paths);
        System.out.println("Entry nodes ************");
        treeStats.getEntries().forEach(System.out::println);
        System.out.println("Terminal nodes *********");
        treeStats.getTerminals().forEach(System.out::println);
        new GraphExporter().exportGraph(treeStats);
    }
}
