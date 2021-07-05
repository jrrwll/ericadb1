package org.ericadb.first.syntax;

import static org.ericadb.first.syntax.SqlAnalyzers.getKeywordOrString;
import static org.ericadb.first.syntax.SqlAnalyzers.getToken;
import static org.ericadb.first.syntax.SqlAnalyzers.throwWrongSyntax;

import java.util.List;
import org.ericadb.first.common.result.Float64ResultObject;
import org.ericadb.first.common.result.Int64ResultObject;
import org.ericadb.first.common.result.TextResultObject;
import org.ericadb.first.common.type.EType;
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
        if (!KeywordToken.CREATE.equalsIgnoreCase(token1)) {
            return null;
        }

        String token2 = tokens.get(1).getKeyword();
        String tableName;
        int offset;
        boolean orReplace = false, ifNotExists = false;
        if (KeywordToken.TABLE.equalsIgnoreCase(token2)) {
            tableName = getKeywordOrString(tokens, 2);
            offset = 3;
        } else if (KeywordToken.OR.equalsIgnoreCase(token2) && size >= 9
                && KeywordToken.REPLACE.equalsIgnoreCase(tokens.get(2).getKeyword())
                && KeywordToken.TABLE.equalsIgnoreCase(tokens.get(3).getKeyword())) {
            tableName = getKeywordOrString(tokens, 4);
            offset = 5;
            orReplace = true;
        } else if (KeywordToken.IF.equalsIgnoreCase(token2) && size >= 10
                && KeywordToken.NOT.equalsIgnoreCase(tokens.get(2).getKeyword())
                && KeywordToken.EXISTS.equalsIgnoreCase(tokens.get(3).getKeyword())
                && KeywordToken.TABLE.equalsIgnoreCase(tokens.get(4).getKeyword())) {
            tableName = getKeywordOrString(tokens, 5);
            offset = 6;
            ifNotExists = true;
        } else {
            return null;
        }

        CreateTableSqlObject sqlObject = new CreateTableSqlObject(
                lexTokens.getSql(), tableName, orReplace, ifNotExists);
        // add columns
        this.analyseColumns(sqlObject, tokens, offset);
        return sqlObject;
    }

    private void analyseColumns(CreateTableSqlObject sqlObject, List<LexToken> tokens, int offset) {
        if (!PunctuationToken.LEFT_PARENTHESIS.equals(getToken(tokens, offset))) {
            throwWrongSyntax(tokens, offset);
        }

        List<ColumnDefinition> columnDefinitions = sqlObject.getColumnDefinitions();
        outer:
        for (; ; ) {
            LexToken token1 = getToken(tokens, ++offset);
            String columnName = getKeywordOrString(token1, tokens, ++offset);

            LexToken token2 = getToken(tokens, ++offset);
            String typeName;
            EType type;
            if ((typeName = token2.getKeyword()) == null
                    || (type = KeywordToken.parseType(typeName)) == null) {
                throwWrongSyntax(tokens, offset - 1);
                return;
            }
            ColumnDefinition columnDefinition = new ColumnDefinition(columnName, type);
            columnDefinitions.add(columnDefinition);

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
                }

                throwWrongSyntax(tokens, offset);
            }
        }

        // finish analyze (...)
    }
}
