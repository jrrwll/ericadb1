package org.ericadb.first.syntax;

import org.dreamcat.common.x.jackson.JsonUtil;
import org.ericadb.first.TestBase;
import org.ericadb.first.sql.SqlObject;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-07-30
 */
class InsertIntoSqlAnalyzerTest extends TestBase {

    @Test
    void analyse() {
        doAnalyse("insert into jerry\n"
                + "values (1, 'a')");

        doAnalyse("insert into jerry\n"
                + "values (1, 'a');");

        doAnalyse("insert into jerry\n"
                + "values (1, 'a'),\n"
                + "       (2, 'b')");

        doAnalyse("insert into jerry(id, `word`)\n"
                + "values (1, 'a'),\n"
                + "       (2, 'b')");

        doAnalyse("insert into will.jerry(id, `word`)\n"
                + "values (1, 'a'),\n"
                + "       (2, 'b')");
    }

    void doAnalyse(String sql) {
        SqlObject sqlObject = SqlAnalyzer.analyse(lex(sql));
        System.out.println(JsonUtil.toJson(sqlObject));
    }
}
