package org.ericadb.first;

import org.dreamcat.common.x.jackson.JsonUtil;
import org.dreamcat.round.lex.Lexer;
import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.lex.SqlLexSettings;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.syntax.SqlAnalyzer;

/**
 * @author Jerry Will
 * @version 2021-07-30
 */
public class TestBase {

    protected Lexer sqlLexer = new Lexer(new SqlLexSettings());

    public TokenInfoStream lex(String sql) {
        return sqlLexer.lex(sql);
    }

    public void analyse(String sql) {
        SqlObject sqlObject = SqlAnalyzer.analyse(lex(sql));
        System.out.println(JsonUtil.toJson(sqlObject));
    }

}
