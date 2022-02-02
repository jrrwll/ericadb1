package org.ericadb.first.syntax;

import org.dreamcat.common.bean.BeanUtil;
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
        SqlObject sqlObject = SqlAnalyzer.syntax(lex("insert into jerry(id, `word`)\n"
                + "values (1, 'a'),\n"
                + "       (2, 'b')"));
        System.out.println(BeanUtil.pretty(sqlObject));
    }
}
