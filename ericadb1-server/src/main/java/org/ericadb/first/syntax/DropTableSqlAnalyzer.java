package org.ericadb.first.syntax;

import java.util.List;
import org.ericadb.first.lexer.KeywordToken;
import org.ericadb.first.lexer.LexToken;
import org.ericadb.first.lexer.LexTokens;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.definition.DropTableSqlObject;

/**
 * @author Jerry Will
 * @version 2021-07-22
 */
public class DropTableSqlAnalyzer implements SqlAnalyzer {

    /**
     * drop table $tableName
     * drop if exists table $tableName
     * <p>
     * drop table $databaseName.$tableName
     * drop if exists table $databaseName.$tableName
     *
     * @param lexTokens sql tokens
     * @return sql object
     */
    @Override
    public SqlObject analyse(LexTokens lexTokens) {
        List<LexToken> tokens = lexTokens.getTokens();
        int size = tokens.size();
        // note that only size = {3, 5, 7} is supported
        if (size != 3 && size != 5 && size != 7) return null;

        String token1 = tokens.get(0).getKeyword();
        if (!KeywordToken.DROP.equalsIgnoreCase(token1)) {
            return null;
        }

        String token2 = tokens.get(1).getKeyword();
        DropTableSqlObject sqlObject = new DropTableSqlObject(lexTokens.getSql());
        if (KeywordToken.TABLE.equals(token2) && size == 3) {
            sqlObject.analyseBetweenDot(tokens, 3);
        } else if (KeywordToken.IF.equals(token2) && size >= 5
                && KeywordToken.EXISTS.equals(tokens.get(2).getKeyword())
                && KeywordToken.TABLE.equals(tokens.get(3).getKeyword())) {
            sqlObject.analyseBetweenDot(tokens, 5);
            sqlObject.setIfExists(true);
        } else {
            return null;
        }

        return sqlObject;
    }
}
