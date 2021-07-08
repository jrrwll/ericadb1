package org.ericadb.first.syntax;

import static org.ericadb.first.syntax.SqlAnalyzers.getKeywordOrString;
import static org.ericadb.first.syntax.SqlAnalyzers.getToken;
import static org.ericadb.first.syntax.SqlAnalyzers.throwWrongSyntax;

import java.util.Collections;
import java.util.List;
import org.ericadb.first.common.result.Float64ResultObject;
import org.ericadb.first.common.result.Int64ResultObject;
import org.ericadb.first.common.result.TextResultObject;
import org.ericadb.first.common.type.EType;
import org.ericadb.first.exception.EricaException;
import org.ericadb.first.lexer.KeywordToken;
import org.ericadb.first.lexer.LexToken;
import org.ericadb.first.lexer.LexTokens;
import org.ericadb.first.lexer.PunctuationToken;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.definition.ColumnDefinition;
import org.ericadb.first.sql.definition.CreateTableSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
public class CreateTableSqlAnalyzer implements SqlAnalyzer {

    /**
     * create if not exists table $tableName(...)
     * create or replace table  $tableName(...)
     * create table $tableName(...)
     * <p>
     * create if not exists table $databaseName.$tableName(...)
     * create or replace table  $databaseName.$tableName(...)
     * create table $databaseName.$tableName(...)
     *
     * @param lexTokens sql tokens
     * @return sql object
     */
    @Override
    public SqlObject analyse(LexTokens lexTokens) {
        List<LexToken> tokens = lexTokens.getTokens();

        int size = tokens.size();
        // note that only size >= 7 is supported
        if (size < 7) return null;

        String token1 = tokens.get(0).getKeyword();
        if (!KeywordToken.CREATE.equals(token1)) {
            return null;
        }

        String token2 = tokens.get(1).getKeyword();
        int offset;
        CreateTableSqlObject sqlObject = new CreateTableSqlObject(lexTokens.getSql());
        boolean orReplace = false, ifNotExists = false;
        if (KeywordToken.TABLE.equals(token2)) {
            offset = analyseBetweenDot(sqlObject, tokens, 3);
        } else if (KeywordToken.OR.equals(token2) && size >= 9
                && KeywordToken.REPLACE.equals(tokens.get(2).getKeyword())
                && KeywordToken.TABLE.equals(tokens.get(3).getKeyword())) {
            offset = analyseBetweenDot(sqlObject, tokens, 5);
            orReplace = true;
        } else if (KeywordToken.IF.equals(token2) && size >= 10
                && KeywordToken.NOT.equals(tokens.get(2).getKeyword())
                && KeywordToken.EXISTS.equals(tokens.get(3).getKeyword())
                && KeywordToken.TABLE.equals(tokens.get(4).getKeyword())) {
            offset = analyseBetweenDot(sqlObject, tokens, 6);
            ifNotExists = true;
        } else {
            return null;
        }
        sqlObject.setOrReplace(orReplace);
        sqlObject.setIfNotExists(ifNotExists);

        // add columns
        offset = this.analyseColumns(sqlObject, tokens, offset);
        if (sqlObject.getColumnDefinitions().isEmpty()) {
            throwWrongSyntax(tokens, offset);
        }
        return sqlObject;
    }

    private int analyseBetweenDot(CreateTableSqlObject sqlObject, List<LexToken> tokens, int dotIndex) {
        if (PunctuationToken.DOT.equals(tokens.get(dotIndex))) {
            sqlObject.setDatabaseName(getKeywordOrString(tokens, dotIndex - 1));
            sqlObject.setTableName(getKeywordOrString(tokens, dotIndex + 1));
            return dotIndex + 2;
        } else {
            sqlObject.setTableName(getKeywordOrString(tokens, dotIndex - 1));
            return dotIndex;
        }
    }

    private int analyseColumns(CreateTableSqlObject sqlObject, List<LexToken> tokens, int offset) {
        if (!PunctuationToken.LEFT_PARENTHESIS.equals(getToken(tokens, offset))) {
            throwWrongSyntax(tokens, offset);
        }

        List<ColumnDefinition> columnDefinitions = sqlObject.getColumnDefinitions();
        int columnIndex = 1;
        List<String> primaryColumnNames = null;
        outer:
        for (; ; ) {
            LexToken token1 = getToken(tokens, ++offset);
            String columnName = getKeywordOrString(token1, tokens, ++offset);

            LexToken token2 = getToken(tokens, ++offset);
            String typeName;
            EType type;
            if ((typeName = token2.getKeyword()) == null
                    || (type = KeywordToken.parseType(typeName)) == null) {
                return throwWrongSyntax(tokens, offset - 1);
            }
            ColumnDefinition columnDefinition = new ColumnDefinition(columnName, type);
            columnDefinitions.add(columnDefinition);
            columnDefinition.setIndex(columnIndex++);
            for (; ; ) {
                LexToken token3 = getToken(tokens, ++offset);
                if (token3.equals(PunctuationToken.RIGHT_PARENTHESIS)) {
                    break outer;
                }
                if (token3.equals(PunctuationToken.COMMA)) {
                    break;
                }
                String k3 = token3.getKeyword();
                if (KeywordToken.NULL.equals(k3)) {
                    continue;
                } else if (KeywordToken.NOT.equals(k3)) {
                    LexToken token4 = getToken(tokens, ++offset);
                    if (KeywordToken.NULL.equals(token4.getKeyword())) {
                        columnDefinition.setNotNull(true);
                        continue;
                    }
                } else if (KeywordToken.DEFAULT.equals(k3)) {
                    LexToken token4 = getToken(tokens, ++offset);
                    if (token4.isLiteralString()) {
                        columnDefinition.setDefaultValue(
                                new TextResultObject(token4.getString()));
                    } else if (token4.isLiteralInteger()) {
                        columnDefinition.setDefaultValue(
                                new Int64ResultObject(token4.getInteger()));
                    } else if (token4.isLiteralFloat()) {
                        columnDefinition.setDefaultValue(
                                new Float64ResultObject(token4.getFloat()));
                    }
                } else if (KeywordToken.PRIMARY.equals(k3)) {
                    LexToken token4 = getToken(tokens, ++offset);
                    if (KeywordToken.PRIMARY.equals(token4.getKeyword())) {
                        if (primaryColumnNames == null) {
                            primaryColumnNames = Collections.singletonList(columnName);
                        } else {
                            throw new EricaException("Duplicated primary key: " + columnName);
                        }
                    }
                }

                throwWrongSyntax(tokens, offset);
            }
        }

        // finish analyze (...)
        return offset;
    }
}
