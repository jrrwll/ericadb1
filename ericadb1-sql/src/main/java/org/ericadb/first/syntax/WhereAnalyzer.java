package org.ericadb.first.syntax;

import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.manipulation.DeleteFromSqlObject;
import org.ericadb.first.sql.query.WhereSqlObject;

/**
 * @author Jerry Will
 * @version 2021-07-22
 */
public class WhereAnalyzer {

    static WhereSqlObject analyse(TokenInfoStream stream) {
        WhereSqlObject sqlObject = new WhereSqlObject();

        return sqlObject;
    }
}
