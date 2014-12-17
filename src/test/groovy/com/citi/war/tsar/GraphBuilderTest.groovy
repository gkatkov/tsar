package com.citi.war.tsar

/**
 * @author sovsyankin
 * @since 17.12.2014
 */

class GraphBuilderTest {

    @org.junit.Test
    void 'build straight flow with single terminal'() {
        def builder = new GraphBuilder(index([[system("o1"), system("a"), system("b"), system("c"), system("t1")]]),
                ["o1"] as Set, ["t1"] as Set)
        def edges = builder.construct()
        assert edges == [edge("o1", "a"), edge("a", "b"), edge("b", "c"), edge("c", "t1")] as Set
    }

    @org.junit.Test
    void 'build straight flow with two terminals'() {
        def builder = new GraphBuilder(index([[system("o1"), system("a"), system("b"), system("t1"), system("t2")]]),
                ["o1"] as Set, ["t1", "t2"] as Set)
        def edges = builder.construct()

        assert edges == [edge("o1", "a"), edge("a", "b"), edge("b", "t1"), edge("b", "t2")] as Set

        builder = new GraphBuilder(index([[system("o1"), system("a"), system("t1"), system("b"), system("t2")]]),
                ["o1"] as Set, ["t1", "t2"] as Set)
        edges = builder.construct()

        assert edges == [edge("o1", "a"), edge("a", "t1"), edge("a", "b"), edge("b", "t2")] as Set
    }

    @org.junit.Test
    void 'build fork with two terminals'() {
        def builder = new GraphBuilder(index([
                [system("o1"), system("a"), system("t1")],
                [system("o1"), system("b"), system("t1")]
        ]), ["o1"] as Set, ["t1", "t2"] as Set)
        def edges = builder.construct()

        assert edges == [edge("o1", "a"), edge("a", "t1"), edge("o1", "b"), edge("b", "t1"), ] as Set
    }

    private static GraphBuilder.Edge edge(String from, String to, int weight = 1) {
        def edge = new GraphBuilder.Edge(from, to)
        edge.inc()
        edge
    }

    private static Index index(List<List<SystemDate>> paths) {
        def i = 0;
        def facts = paths.collectEntries { path -> [([("key" + i++): ""]): path] }
        new Index(facts, facts.keySet(), [[:]] as Set)
    }

    static SystemDate system(String name) {
        new SystemDate(name, 0l);
    }
}
