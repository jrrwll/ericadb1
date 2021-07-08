package org.ericadb.first.server;

import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.lexer.LexTokens;
import org.ericadb.first.lexer.SqlLexer;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.syntax.SqlAnalyzers;

/**
 * @author Jerry Will
 * @since 2021/6/11
 */
public interface EricaServer {

    default ResultObjects execute(String sql, SqlContext context) {
        SqlLexer lexer = new SqlLexer();
        // do lexer
        LexTokens tokens = lexer.lex(sql);

        // do syntax
        SqlObject sqlObject = SqlAnalyzers.analyse(tokens);

        // do optimize
        // nop

        // do execute
        return sqlObject.execute(context);
    }
}
