package org.ericadb.first.syntax;


import static org.ericadb.first.lex.KeywordToken.*;
import static org.ericadb.first.syntax.Companion.*;

import java.util.List;
import org.dreamcat.round.lex.RoundToken;
import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.definition.ColumnDefinition;
import org.ericadb.first.sql.definition.CreateTableSqlObject;
import org.ericadb.first.sql.definition.DropTableSqlObject;
import org.ericadb.first.sql.definition.IndexDefinition;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
class TableAnalyzer {

    /**
     * create table if not exists $tableName(...)
     * create table $tableName(...)
     * <p>
     * create table if not exists $databaseName.$tableName(...)
     * create table $databaseName.$tableName(...)
     *
     * @param stream sql tokens
     * @return sql object
     */
    static SqlObject analyseCreateTable(TokenInfoStream stream) {
        RoundToken token = stream.next();
        CreateTableSqlObject sqlObject = new CreateTableSqlObject(stream.getExpression());
        if (isKeyword(token, IF)) {
            if (isNotKeyword(stream.next(), NOT) && isNotKeyword(stream.next(), EXISTS)) {
                return stream.throwWrongSyntax();
            }
            sqlObject.setIfNotExists(true);
            stream.next();
        }

        // $databaseName.$tableName
        analyseDatabaseAndTableName(stream,
                sqlObject::setDatabaseName, sqlObject::setTableName);

        if (!stream.next().isLeftParenthesis()) stream.throwWrongSyntax();

        // column
        boolean defEnd = false;
        token = stream.next();
        if (isIndexDefStartToken(token)) return stream.throwWrongSyntax();
        for (;;) {
            ColumnDefinition column = DefAnalyzer.analyseColumnDef(stream);
            sqlObject.getColumnDefinitions().add(column);
            token = stream.next();
            if (token.isRightParenthesis()) {
                defEnd = true;
                break;
            } else if (!token.isComma()){
                return stream.throwWrongSyntax();
            }
            token = stream.next();
            if (isIndexDefStartToken(token)) break;
        }

        if (!defEnd) {
            token = stream.next();
            for (;;) {
                if (isKeyword(token, PRIMARY)) {
                    if (sqlObject.getPrimaryColumnNames() != null) {
                        stream.previous();
                        return stream.throwWrongSyntax();
                    }
                    if (isNotKeyword(token, KEY) && !stream.next().isLeftParenthesis()) {
                        return stream.throwWrongSyntax();
                    }
                    List<String> columnNames = DefAnalyzer.analyseColumnNames(stream);
                    if (!stream.next().isRightParenthesis()) return stream.throwWrongSyntax();
                    sqlObject.setPrimaryColumnNames(columnNames);
                } else if (isKeyword(token, KEY) || isKeyword(token, INDEX)) {
                    IndexDefinition index = DefAnalyzer.analyseIndexDef(stream);
                    sqlObject.getIndexDefinitions().add(index);
                } else if (isKeyword(token, UNIQUE)) {
                    if (isNotKeyword(token = stream.next(), KEY) && isNotKeyword(token, INDEX)) {
                        return stream.throwWrongSyntax();
                    }
                    IndexDefinition index = DefAnalyzer.analyseIndexDef(stream);
                    index.setUnique(true);
                    sqlObject.getIndexDefinitions().add(index);
                } else {
                    return stream.throwWrongSyntax();
                }
            }
        }
        stream.next();
        return sqlObject;
    }

    /**
     * drop table $tableName
     * drop table if exists $tableName
     * <p>
     * drop table $databaseName.$tableName
     * drop table if exists $databaseName.$tableName
     *
     * @param stream sql tokens
     * @return sql object
     */
    static SqlObject analyseDropTable(TokenInfoStream stream) {
        DropTableSqlObject sqlObject = new DropTableSqlObject(stream.getExpression());

        RoundToken token = stream.next();
        if (isKeyword(token, IF)) {
            if (isNotKeyword(token, EXISTS)) return stream.throwWrongSyntax();
            sqlObject.setIfExists(true);
            stream.next();
        }

        // $databaseName.$tableName
        analyseDatabaseAndTableName(stream,
                sqlObject::setDatabaseName, sqlObject::setTableName);

        return sqlObject;
    }

}
