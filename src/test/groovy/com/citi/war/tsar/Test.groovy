package com.citi.war.tsar

import java.util.function.Supplier

/**
 * Created by GKatkov on 16.12.2014.
 */
class Test {

    @org.junit.Test
    void "test" () {
        def datePrev
        long counter = 0
        def List<Map> maps = []

        def file = new BufferedReader(new FileReader("src/test/resources/output-sample.log"))
        def paths = ParseUtils.collectPaths(new Supplier<LogEntry>() {
            @Override
            LogEntry get() {
                def line = file.readLine()
                if (line == null) return null;
                return ParseUtils.parseLogEntry(line)
            }
        })
        def tree = new TreeBuilder().buildTree(paths);
//        file.eachLine {
//            counter++
//            if (counter % 10000 == 0) println counter;
//        }
    }

    static boolean isASubMap(Map a, Map b) {
        return a.findAll {k, v ->
            b.containsKey(k) && b.get(k) == v
        }.size() == a.size()
    }

    static Map parseEntry(String entry) {
        def cells = entry.split(" ", 4)
        def date = new Date(Long.valueOf(cells[0]))
        def system = cells[1]
        def attrsString = cells[3][5..(cells[3].size() - 3)]
        def fact = attrsString[1..(attrsString.size() - 1)].split(",").collectEntries {
            def a = it.trim().split(":")
            [(a[0]): a[1]]
        }
        return [date: date, system: system, fact: fact]
    }
}
