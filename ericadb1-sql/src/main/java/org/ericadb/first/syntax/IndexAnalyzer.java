package org.ericadb.first.syntax;


import static org.ericadb.first.lex.KeywordToken.ON;
import static org.ericadb.first.syntax.Companion.analyseDatabaseAndTableName;
import static org.ericadb.first.syntax.Companion.getIdentifierOrBacktick;
import static org.ericadb.first.syntax.Companion.isNotKeyword;

import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.definition.CreateIndexSqlObject;
import org.ericadb.first.sql.definition.DropIndexSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
class IndexAnalyzer {

    /**
     * create [unique] index $indexName on $tableName (c_1, `c_2`)
     *
     * @param stream sql tokens
     * @return sql object
     */
    static CreateIndexSqlObject analyseCreateIndex(TokenInfoStream stream) {
        CreateIndexSqlObject sqlObject = new CreateIndexSqlObject(stream.getExpression());

        sqlObject.setIndexName(getIdentifierOrBacktick(stream));
        if (isNotKeyword(stream.next(), ON)) return stream.throwWrongSyntax();

        analyseDatabaseAndTableName(stream, sqlObject::setDatabaseName, sqlObject::setTableName);
        if (!stream.next().isLeftParenthesis()) return stream.throwWrongSyntax();
        sqlObject.setColumnNames(DefAnalyzer.analyseColumnNames(stream));
        if (!stream.next().isRightParenthesis()) return stream.throwWrongSyntax();
        return sqlObject;
    }


    /**
     * drop index $indexName on $tableName
     *
     * @param stream sql tokens
     * @return sql object
     */
    static SqlObject analyseDropIndex(TokenInfoStream stream) {
        DropIndexSqlObject sqlObject = new DropIndexSqlObject(stream.getExpression());

        sqlObject.setIndexName(getIdentifierOrBacktick(stream));
        if (isNotKeyword(stream.next(), ON)) return stream.throwWrongSyntax();

        analyseDatabaseAndTableName(stream, sqlObject::setDatabaseName, sqlObject::setTableName);

        return sqlObject;
    }
}
