package org.ericadb.first.syntax;

import static org.ericadb.first.syntax.SqlAnalyzers.getKeywordOrString;

import java.util.List;
import org.ericadb.first.lexer.KeywordToken;
import org.ericadb.first.lexer.LexToken;
import org.ericadb.first.lexer.LexTokens;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.definition.CreateDatabaseSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
public class CreateDatabaseSqlAnalyzer implements SqlAnalyzer {

    /**
     * create if not exists database $databaseName
     * create or replace database  $databaseName
     * create database $databaseName
     *
     * @param lexTokens sql tokens
     * @return sql object
     */
    @Override
    public SqlObject analyse(LexTokens lexTokens) {
        List<LexToken> tokens = lexTokens.getTokens();
        int size = tokens.size();
        // note that only size = {3, 5, 6} is supported just for now
        if (size > 6 || size == 4 || size < 3) return null;

        String token1 = tokens.get(0).getKeyword();
        if (!KeywordToken.CREATE.equalsIgnoreCase(token1)) {
            return null;
        }

        String token2 = tokens.get(1).getKeyword();
        String databaseName;
        boolean orReplace = false, ifNotExists = false;
        if (KeywordToken.DATABASE.equalsIgnoreCase(token2) && size == 3) {
            databaseName = getKeywordOrString(tokens, 2);
        } else if (KeywordToken.OR.equalsIgnoreCase(token2) && size == 5
                && KeywordToken.REPLACE.equalsIgnoreCase(tokens.get(2).getKeyword())
                && KeywordToken.DATABASE.equalsIgnoreCase(tokens.get(3).getKeyword())) {
            databaseName = getKeywordOrString(tokens, 4);
            orReplace = true;
        } else if (KeywordToken.IF.equalsIgnoreCase(token2) && size == 6
                && KeywordToken.NOT.equalsIgnoreCase(tokens.get(2).getKeyword())
                && KeywordToken.EXISTS.equalsIgnoreCase(tokens.get(3).getKeyword())
                && KeywordToken.DATABASE.equalsIgnoreCase(tokens.get(4).getKeyword())) {
            databaseName = getKeywordOrString(tokens, 5);
            ifNotExists = true;
        } else {
            return null;
        }

        return new CreateDatabaseSqlObject(
                lexTokens.getSql(), databaseName, orReplace, ifNotExists);
    }

}
