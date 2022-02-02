package org.ericadb.first.syntax;

import static org.ericadb.first.syntax.SqlAnalyzers.getKeywordOrString;

import java.util.List;
import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.lexer.KeywordToken;
import org.ericadb.first.lexer.LexToken;
import org.ericadb.first.lexer.LexTokens;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.definition.DropDatabaseSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
 class DropDatabaseSqlAnalyzer {

    /**
     * drop if exists database $databaseName
     * drop database  $databaseName
     *
     * @param stream sql tokens
     * @return sql object
     */
    static SqlObject analyse(TokenInfoStream stream) {
        List<LexToken> tokens = lexTokens.getTokens();
        int size = tokens.size();
        // note that only size = {3, 5} is supported just for now
        if (size != 3 && size != 5) return null;

        String token1 = tokens.get(0).getKeyword();
        if (!KeywordToken.DROP.equalsIgnoreCase(token1)) {
            return null;
        }

        String token2 = tokens.get(1).getKeyword();
        String databaseName;
        boolean ifExists = false;
        if (KeywordToken.DATABASE.equalsIgnoreCase(token2) && size == 3) {
            databaseName = getKeywordOrString(tokens, 2);
        } else if (KeywordToken.IF.equalsIgnoreCase(token2) && size == 5
                && KeywordToken.EXISTS.equalsIgnoreCase(tokens.get(2).getKeyword())
                && KeywordToken.DATABASE.equalsIgnoreCase(tokens.get(3).getKeyword())) {
            databaseName = getKeywordOrString(tokens, 4);
            ifExists = true;
        } else {
            return null;
        }

        return new DropDatabaseSqlObject(lexTokens.getSql(), databaseName, ifExists);
    }
}
