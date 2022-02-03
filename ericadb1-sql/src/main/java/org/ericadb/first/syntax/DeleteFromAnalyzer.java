package org.ericadb.first.syntax;

import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.manipulation.DeleteFromSqlObject;

/**
 * @author Jerry Will
 * @version 2021-07-22
 */
public class DeleteFromAnalyzer {

    static SqlObject analyse(TokenInfoStream stream) {
        DeleteFromSqlObject sqlObject = new DeleteFromSqlObject(stream.getExpression());

        return sqlObject;
    }
}
