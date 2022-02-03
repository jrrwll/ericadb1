package org.ericadb.first.syntax;

import org.ericadb.first.TestBase;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-07-30
 */
class ManipulationAnalyzerTest extends TestBase {

    @Test
    void insertInto() {
        analyse("insert into jerry\n"
                + "values (1, 'a')");

        analyse("insert into jerry\n"
                + "values (1, 'a');");

        analyse("insert into jerry\n"
                + "values (1, 'a'),\n"
                + "       (2, 'b')");

        analyse("insert into jerry(id, `word`)\n"
                + "values (1, 'a'),\n"
                + "       (2, 'b')");

        analyse("insert into will.jerry(id, `word`)\n"
                + "values (1, 'a'),\n"
                + "       (2, 'b')");
    }
}
