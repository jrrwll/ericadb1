package org.ericadb.first.syntax;

import java.util.Arrays;
import java.util.List;
import org.dreamcat.common.util.CollectionUtil;
import org.ericadb.first.exception.EricaException;
import org.ericadb.first.lexer.AbsLexToken;
import org.ericadb.first.lexer.LexToken;
import org.ericadb.first.lexer.LexTokens;
import org.ericadb.first.sql.SqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
public final class SqlAnalyzers {

    private SqlAnalyzers() {
    }

    private static final List<SqlAnalyzer> analyzers;

    public static SqlObject analyse(LexTokens lexTokens) {
        for (SqlAnalyzer analyzer : analyzers) {
            SqlObject sqlObject = analyzer.analyse(lexTokens);
            if (sqlObject != null) return sqlObject;
        }

        return throwWrongSyntax(lexTokens.getTokens(), 0);
    }

    public static <T> T throwWrongSyntax(List<LexToken> tokens, int index) {
        throw new EricaException("You has wrong syntax in your SQL, near at: " +
                AbsLexToken.getRawToken(CollectionUtil.sample(tokens, index, 2)));
    }

    public static LexToken getToken(List<LexToken> tokens, int index) {
        int size;
        if (index >= (size = tokens.size())) {
            throwWrongSyntax(tokens, size - 1);
        }
        return tokens.get(index);
    }

    public static String getKeywordOrString(List<LexToken> tokens, int index) {
        return getKeywordOrString(tokens.get(index), tokens, index);
    }

    public static String getKeywordOrString(LexToken token, List<LexToken> tokens, int index) {
        if (token.isKeyword()) {
            return token.getKeyword();
        } else if (token.isString()) {
            return token.getString();
        } else {
            return throwWrongSyntax(tokens, index);
        }
    }

    static {
        analyzers = Arrays.asList(
                // ddl
                new CreateDatabaseSqlAnalyzer(),
                new DropDatabaseSqlAnalyzer(),
                new CreateTableSqlAnalyzer(),
                new DropTableSqlAnalyzer(),
                // dml
                new InsertIntoSqlAnalyzer(),
                new UpdateSetSqlAnalyzer(),
                new DeleteFromSqlAnalyzer(),
                // query
                new SelectFromSqlAnalyzer()
        );
    }
}
