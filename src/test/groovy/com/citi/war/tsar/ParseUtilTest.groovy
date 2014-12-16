package com.citi.war.tsar

/**
 * @author sovsyankin
 * @since 16.12.2014
 */
class ParseUtilTest {

    @org.junit.Test
    void "regular log entry must be parsed"() {
        def entry = ParseUtils.parseLogEntry("28348104607576 L_1_Sys_3 processed Fact{[at7DE:368177, attRL:UGVv6N]}")

        assert entry.date == new Date(Long.parseLong("28348104607576"))
        assert entry.system == "L_1_Sys_3"
        assert entry.attributes == [
                at7DE:'368177',
                attRL:'UGVv6N'
        ]
    }
}
