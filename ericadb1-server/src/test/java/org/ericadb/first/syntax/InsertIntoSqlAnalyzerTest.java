package org.ericadb.first.syntax;

import org.dreamcat.common.bean.BeanFormatUtil;
import org.ericadb.first.TestBase;
import org.ericadb.first.sql.SqlObject;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-07-30
 */
class InsertIntoSqlAnalyzerTest extends TestBase {

    SqlAnalyzer sqlAnalyzer = new InsertIntoSqlAnalyzer();

    @Test
    void analyse() {
        SqlObject sqlObject = sqlAnalyzer.analyse(lex("insert into jerry(id, word)\n"
                + "values (1, 'a'),\n"
                + "       (2, 'b')"));
        System.out.println(BeanFormatUtil.pretty(sqlObject));
    }
}
