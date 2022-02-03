package org.ericadb.first.syntax;

import static org.ericadb.first.syntax.Companion.analyseDatabaseAndTableName;
import static org.ericadb.first.syntax.Companion.getIdentifierOrBacktick;

import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.configuration.UseSqlObject;
import org.ericadb.first.sql.manipulation.TruncateSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
class OtherAnalyzer {

    /**
     * use $databaseName
     *
     * @param stream sql tokens
     * @return sql object
     */
    static SqlObject analyseUse(TokenInfoStream stream) {
        UseSqlObject sqlObject = new UseSqlObject(stream.getExpression());
        sqlObject.setDatabaseName(getIdentifierOrBacktick(stream));
        return sqlObject;
    }

    /**
     * truncate $tableName
     *
     * @param stream sql tokens
     * @return sql object
     */
    static SqlObject analyseTruncate(TokenInfoStream stream) {
        TruncateSqlObject sqlObject = new TruncateSqlObject(stream.getExpression());
        analyseDatabaseAndTableName(stream, sqlObject::setDatabaseName, sqlObject::setTableName);
        return sqlObject;
    }
}
