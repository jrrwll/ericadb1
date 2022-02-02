package org.ericadb.first;

import org.dreamcat.round.lex.Lexer;
import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.lex.SqlLexSettings;

/**
 * @author Jerry Will
 * @version 2021-07-30
 */
public class TestBase {

    protected Lexer sqlLexer = new Lexer(new SqlLexSettings());

    public TokenInfoStream lex(String sql) {
        return sqlLexer.lex(sql);
    }

}
