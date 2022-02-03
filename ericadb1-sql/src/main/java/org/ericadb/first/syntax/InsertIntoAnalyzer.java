package org.ericadb.first.syntax;

import static org.dreamcat.round.lex.PunctuationToken.COMMA;
import static org.dreamcat.round.lex.PunctuationToken.LEFT_PARENTHESIS;
import static org.dreamcat.round.lex.PunctuationToken.RIGHT_PARENTHESIS;
import static org.dreamcat.round.lex.PunctuationToken.SEMICOLON;
import static org.ericadb.first.lex.KeywordToken.VALUES;
import static org.ericadb.first.syntax.Companion.EMPTY_ARRAY;
import static org.ericadb.first.syntax.Companion.analyseDatabaseAndTableName;
import static org.ericadb.first.syntax.Companion.getIdentifierOrBacktick;
import static org.ericadb.first.syntax.Companion.isNotKeyword;

import java.util.ArrayList;
import java.util.List;
import org.dreamcat.round.lex.RoundToken;
import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.manipulation.InsertIntoSqlObject;

/**
 * @author Jerry Will
 * @version 2021-07-22
 */
class InsertIntoAnalyzer {

    /**
     * insert into [$databaseName.]$tableName[(_1, _i, _m)] values (c1, ci, cm)[, (c1j, cij, cmn)]
     *
     * @param stream sql tokens
     * @return sql object, null if not suitable
     */
    static SqlObject analyse(TokenInfoStream stream) {
        InsertIntoSqlObject sqlObject = new InsertIntoSqlObject(stream.getExpression());

        // $databaseName.$tableName
        analyseDatabaseAndTableName(stream,
                sqlObject::setDatabaseName, sqlObject::setTableName);

        RoundToken token = stream.next();
        if (LEFT_PARENTHESIS.equals(token)) {
            boolean needComma = false;
            while (!RIGHT_PARENTHESIS.equals(token = stream.next())) {
                if (needComma) {
                    if (!COMMA.equals(token)) {
                        return stream.throwWrongSyntax();
                    } else needComma = false;
                } else {
                    sqlObject.getColumnNames().add(getIdentifierOrBacktick(token));
                    needComma = true;
                }
            }
            token = stream.next();
        }

        if (isNotKeyword(token, VALUES)) return stream.throwWrongSyntax();

        token = stream.next();
        int valuesSize = 0;
        List<Object> value = new ArrayList<>();
        while (stream.hasNext()) {
            Object[] fixedValue = null;
            if (valuesSize != 0) {
                fixedValue = new Object[valuesSize];
            }
            if (LEFT_PARENTHESIS.equals(token)) {
                boolean needComma = false;
                int fixedI = 0;
                while (!RIGHT_PARENTHESIS.equals(token = stream.next())) {
                    if (needComma) {
                        if (COMMA.equals(token)) {
                            needComma = false;
                            continue;
                        } else return stream.throwWrongSyntax();
                    }

                    if (!token.isValue()) return stream.throwWrongSyntax();

                    if (valuesSize == 0) {
                        value.add(token.getValue());
                    } else {
                        fixedValue[fixedI++] = token.getValue();
                    }
                    needComma = true;
                }
                if (valuesSize == 0) {
                    fixedValue = value.toArray(EMPTY_ARRAY);
                    valuesSize = fixedValue.length;
                    if (valuesSize == 0) {
                        return stream.throwWrongSyntax();
                    }
                } else if (fixedI != valuesSize) {
                    return stream.throwWrongSyntax();
                }
                sqlObject.getValues().add(fixedValue);

                if (stream.hasNext()) {
                    token = stream.next();

                    if (COMMA.equals(token)) {
                        token = stream.next();
                    } else if (SEMICOLON.equals(token)) {
                        break;
                    } else {
                        return stream.throwWrongSyntax();
                    }
                } else {
                    break;
                }
            }
        }
        return sqlObject;
    }
}
