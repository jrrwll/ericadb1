package org.ericadb.first.syntax.aware;

import static org.ericadb.first.syntax.SqlAnalyzers.getKeywordOrString;

import java.util.List;
import org.ericadb.first.lexer.LexToken;
import org.ericadb.first.lexer.PunctuationToken;

/**
 * @author Jerry Will
 * @version 2021-07-22
 */
public interface DatabaseTableNameAware {

    void setDatabaseName(String databaseName);

    void setTableName(String tableName);

    default int analyseBetweenDot(List<LexToken> tokens, int dotIndex) {
        if (PunctuationToken.DOT.equals(tokens.get(dotIndex))) {
            setDatabaseName(getKeywordOrString(tokens, dotIndex - 1));
            setTableName(getKeywordOrString(tokens, dotIndex + 1));
            return dotIndex + 2;
        } else {
            setTableName(getKeywordOrString(tokens, dotIndex - 1));
            return dotIndex;
        }
    }

}
