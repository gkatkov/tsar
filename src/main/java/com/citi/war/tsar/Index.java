package com.citi.war.tsar;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sovsyankin
 * @since 16.12.2014
 */
public class Index {

    private final Map<Map<String, String>, List<SystemDate>> paths;

    private final Set<Map<String, String>> simple;

    private final Set<Map<String, String>> complex;

    public Index(Map<Map<String, String>, List<SystemDate>> paths, Set<Map<String, String>> simple, Set<Map<String, String>> complex) {
        this.paths = paths;
        this.simple = simple;
        this.complex = complex;
    }

    public Map<Map<String, String>, List<SystemDate>> paths() {
        return paths;
    }

    public Set<Map<String, String>> getSimple() {
        return simple;
    }

    public Set<Map<String, String>> getComplex() {
        return complex;
    }
}
