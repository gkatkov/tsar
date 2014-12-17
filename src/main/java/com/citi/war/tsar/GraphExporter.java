package com.citi.war.tsar;

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;
import org.jgraph.JGraph;
import org.jgrapht.Graph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.ext.ComponentAttributeProvider;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.StringNameProvider;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Throwables.propagate;

/**
 * Created by GKatkov on 17.12.2014.
 */
public class GraphExporter {

    public void exportGraph(TreeStats treeStats) {
        ListenableDirectedWeightedGraph<String, DefaultWeightedEdge> g = new ListenableDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        for (String parent : treeStats.getTree().keySet()) {
            Set<String> children = treeStats.getTree().get(parent);
            children.forEach( child -> {
                if (!g.containsVertex(child)) g.addVertex(child);
            });
            if (parent != null) {
                if (!g.containsVertex(parent)) {
                    g.addVertex(parent);
                }
                children.forEach(child -> {
                    if (!g.containsEdge(parent, child)) {
                        DefaultWeightedEdge defaultWeightedEdge = g.addEdge(parent, child);
                        g.setEdgeWeight(defaultWeightedEdge, 0.2);
                    }
                });
            }
        }
        exportGraph(g);
    }

    static void fillInTheGraph( WeightedGraph<String, DefaultWeightedEdge> g, String parent, Set<String> children) {

    }

    static public void exportGraph(Graph<String, DefaultWeightedEdge> g) {
        StringNameProvider<String> nameProvider = new StringNameProvider<>();
        ComponentAttributeProvider<DefaultWeightedEdge> componentAttributeProvider =
                e -> {
                    Map<String, String> map = new LinkedHashMap<>();
                    map.put("weight", Double.toString(g.getEdgeWeight(e)));
                    return map;
                };
        DOTExporter<String, DefaultWeightedEdge> export = new DOTExporter<>(nameProvider, nameProvider, null, null, componentAttributeProvider);
        try {
            export.export(new FileWriter("graph.dot"), g);
        } catch (IOException e) {
            throw propagate(e);
        }
    }
}
