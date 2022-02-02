package org.ericadb.first.sql;

import java.util.ArrayList;
import java.util.List;
import org.dreamcat.round.lex.Lexer;
import org.dreamcat.round.lex.PunctuationToken;
import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.lex.SqlLexSettings;
import org.ericadb.first.syntax.SqlAnalyzer;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
public interface SqlObject {

    static List<SqlObject> compile(String sql) {
        SqlLexSettings lexSettings = new SqlLexSettings();
        Lexer lexer = new Lexer(lexSettings);
        List<TokenInfoStream> streams = lexer.lex(sql).split(PunctuationToken.SEMICOLON);
        List<SqlObject> sqlObjects = new ArrayList<>(streams.size());
        for (TokenInfoStream stream : streams) {
            sqlObjects.add(SqlAnalyzer.syntax(stream));
        }
        return sqlObjects;
    }

    @Override
    String toString();

    String getSql();
}
