package com.citi.war.tsar

/**
 * Created by GKatkov on 16.12.2014.
 */
class TreeBuilderTest extends GroovyTestCase {

    void "test build tree"() {
        new TreeBuilder().buildTree([
           ['a', 'b', 'c'],
                ['a', 'b', 'c'],
                ['a', 'd', 'c']
        ])
    }
}
