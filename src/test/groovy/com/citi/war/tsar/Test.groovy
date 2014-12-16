package com.citi.war.tsar

/**
 * Created by GKatkov on 16.12.2014.
 */
class Test {

    @org.junit.Test
    void "test" () {
        def datePrev
        new File("src/test/resources/output-sample.log").eachLine {
            def entry = parseEntry(it)
            assert entry.fact.get("at7DE")
        }
    }


    static Map parseEntry(String entry) {
        def cells = entry.split(" ", 4)
        def date = new Date(Long.valueOf(cells[0]))
        def system = cells[1]
        def attrsString = cells[3][5..(cells[3].size() - 2)]
        def fact = attrsString[1..(attrsString.size() - 1)].split(",").collectEntries {
            def a = it.trim().split(":")
            [(a[0]): a[1]]
        }
        return [date: date, system: system, fact: fact]
    }
}
