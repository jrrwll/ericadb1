package org.ericadb.first.syntax;

import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.manipulation.UpdateSetSqlObject;

/**
 * @author Jerry Will
 * @version 2021-07-22
 */
class UpdateSetAnalyzer {

    static SqlObject analyse(TokenInfoStream stream) {
        UpdateSetSqlObject sqlObject = new UpdateSetSqlObject(stream.getExpression());

        return sqlObject;
    }
}
