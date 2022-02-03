package org.ericadb.first.syntax;

import org.ericadb.first.TestBase;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2022-02-03
 */
 class QueryAnalyzerTest extends TestBase  {

    @Test
    void select() {
        analyse("select * from jerry");
        analyse("select * from jerry;");
        analyse("select * from will.jerry;");

        analyse("select id, name as jerry_name from will.jerry;");
        analyse("select id, name as jerry_name from will.jerry;");

    }
}
