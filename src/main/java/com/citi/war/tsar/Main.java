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
        checkArgument(args.length == 0, "Moar args (file name required)");
        checkArgument(args.length > 1, "Too many arguments (only file name required)");
        final String fileName = args[0];
        final FactIndex factIndex = new FactIndex();
        final Map<Map<String, String>, List<SystemDate>> index = factIndex.indexByEntry(fileName);
        System.out.println("# of facts: " + index.size());
    }
}
