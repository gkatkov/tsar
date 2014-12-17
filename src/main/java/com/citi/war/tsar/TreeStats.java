package com.citi.war.tsar;

import java.util.HashMap;
import java.util.Set;

/**
 * @author sovsyankin
 * @since 16.12.2014
 */
public class TreeStats {
    private final HashMap<String, Set<String>> tree;

    private final Set<String> originators;

    private final Set<String> terminals;

    public TreeStats(HashMap<String, Set<String>> tree, Set<String> originators, Set<String> terminals) {

        this.tree = tree;
        this.originators = originators;
        this.terminals = terminals;
    }

    public HashMap<String, Set<String>> getTree() {
        return tree;
    }

    public Set<String> getOriginators() {
        return originators;
    }

    public Set<String> getTerminals() {
        return terminals;
    }
}
