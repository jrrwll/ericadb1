package org.ericadb.first;

import org.ericadb.first.lexer.LexTokens;
import org.ericadb.first.lexer.SqlLexer;

/**
 * @author Jerry Will
 * @version 2021-07-30
 */
public class TestBase {

    protected SqlLexer sqlLexer = new SqlLexer();

    public LexTokens lex(String sql) {
        return sqlLexer.lex(sql);
    }

}
