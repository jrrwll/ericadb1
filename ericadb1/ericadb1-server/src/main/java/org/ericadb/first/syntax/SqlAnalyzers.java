package org.ericadb.first.syntax;

import java.util.Arrays;
import java.util.List;
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
        List<LexToken> sample;
        int size = tokens.size();
        if (size <= 3) {
            sample = tokens;
        } else if (index == size - 1) {
            sample = tokens.subList(size - 3, size);
        } else if (index == 0) {
            sample = tokens.subList(0, 3);
        } else {
            sample = tokens.subList(index - 1, index + 2);
        }
        String s = AbsLexToken.getRawToken(sample);

        throw new EricaException(
                "You has wrong syntax in your SQL, near at: " + s);
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
        } else if (token.isLiteralString()) {
            return token.getString();
        } else {
            return throwWrongSyntax(tokens, index);
        }
    }

    static {
        analyzers = Arrays.asList(
                new CreateDatabaseSqlAnalyzer(),
                new DropDatabaseSqlAnalyzer(),
                new CreateTableSqlAnalyzer()
        );
    }
}
