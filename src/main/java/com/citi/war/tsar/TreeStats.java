package com.citi.war.tsar;

import java.util.HashMap;
import java.util.Set;

/**
 * @author sovsyankin
 * @since 16.12.2014
 */
public class TreeStats {
    private final HashMap<String, Set<String>> tree;

    private final Set<String> entries;

    private final Set<String> terminals;

    public TreeStats(HashMap<String, Set<String>> tree, Set<String> entries, Set<String> terminals) {

        this.tree = tree;
        this.entries = entries;
        this.terminals = terminals;
    }

    public HashMap<String, Set<String>> getTree() {
        return tree;
    }

    public Set<String> getEntries() {
        return entries;
    }

    public Set<String> getTerminals() {
        return terminals;
    }
}
