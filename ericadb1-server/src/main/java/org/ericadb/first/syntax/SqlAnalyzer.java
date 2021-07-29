package org.ericadb.first.syntax;

import org.ericadb.first.lexer.LexTokens;
import org.ericadb.first.sql.SqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
public interface SqlAnalyzer {

    /**
     * sql syntax analyse
     *
     * @param lexTokens sql tokens
     * @return sql object, null if not suitable
     */
    SqlObject analyse(LexTokens lexTokens);
}
