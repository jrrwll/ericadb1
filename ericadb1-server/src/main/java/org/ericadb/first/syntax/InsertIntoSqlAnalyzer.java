package org.ericadb.first.syntax;

import static org.ericadb.first.syntax.SqlAnalyzers.getKeywordOrString;
import static org.ericadb.first.syntax.SqlAnalyzers.getToken;
import static org.ericadb.first.syntax.SqlAnalyzers.throwWrongSyntax;

import java.util.ArrayList;
import java.util.List;
import org.ericadb.first.common.result.ResultObject;
import org.ericadb.first.lexer.KeywordToken;
import org.ericadb.first.lexer.LexToken;
import org.ericadb.first.lexer.LexTokens;
import org.ericadb.first.lexer.PunctuationToken;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.manipulation.InsertIntoSqlObject;

/**
 * @author Jerry Will
 * @version 2021-07-22
 */
public class InsertIntoSqlAnalyzer implements SqlAnalyzer {

    /**
     * insert into [$databaseName.]$tableName[(_1, _i, _m)] values (c1, ci, cm)[, (c1j, cij, cmn)]
     *
     * @param lexTokens sql tokens
     * @return sql object, null if not suitable
     */
    @Override
    public SqlObject analyse(LexTokens lexTokens) {
        List<LexToken> tokens = lexTokens.getTokens();
        int size = tokens.size();
        // note that only size >= 7 is supported
        if (size < 7) return null;

        if (!KeywordToken.INSERT.equals(tokens.get(0).getKeyword())
                || !KeywordToken.INTO.equals(tokens.get(1).getKeyword())) {
            return null;
        }

        InsertIntoSqlObject sqlObject = new InsertIntoSqlObject(lexTokens.getSql());
        int offset = sqlObject.analyseBetweenDot(tokens, 3);

        LexToken token = getToken(tokens, offset);
        if (PunctuationToken.LEFT_PARENTHESIS.equals(token)) {
            boolean needComma = false;
            while (!PunctuationToken.RIGHT_PARENTHESIS.equals(token = getToken(tokens, ++offset))) {
                if (needComma) {
                    if (!PunctuationToken.COMMA.equals(token)) {
                        return throwWrongSyntax(tokens, offset);
                    } else needComma = false;
                } else {
                    sqlObject.getColumnNames().add(getKeywordOrString(token, tokens, offset));
                    needComma = true;
                }
            }
        }

        token = getToken(tokens, ++offset);
        if (!KeywordToken.VALUES.equals(token.getKeyword())) {
            throwWrongSyntax(tokens, offset);
        }

        token = getToken(tokens, ++offset);
        int valuesSize = 0;
        List<ResultObject> value = new ArrayList<>();
        while (offset < size) {
            ResultObject[] fixedValue = null;
            if (valuesSize != 0) {
                fixedValue = new ResultObject[valuesSize];
            }
            if (PunctuationToken.LEFT_PARENTHESIS.equals(token)) {
                boolean needComma = false;
                int fixedI = 0;
                while (!PunctuationToken.RIGHT_PARENTHESIS.equals(token = getToken(tokens, ++offset))) {
                    if (needComma) {
                        if (PunctuationToken.COMMA.equals(token)) {
                            needComma = false;
                            continue;
                        } else return throwWrongSyntax(tokens, offset);
                    }

                    if (!token.isValue()) return throwWrongSyntax(tokens, offset);

                    if (valuesSize == 0) {
                        value.add(token.getValue());
                    } else {
                        fixedValue[fixedI++] = token.getValue();
                    }
                    needComma = true;
                }
                if (valuesSize == 0) {
                    fixedValue = value.toArray(ResultObject.EMPTY_ARRAY);
                    valuesSize = fixedValue.length;
                    if (valuesSize == 0) {
                        return throwWrongSyntax(tokens, offset);
                    }
                } else if (fixedI != valuesSize) {
                    return throwWrongSyntax(tokens, offset);
                }
                sqlObject.getValues().add(fixedValue);

                if (offset < size - 1) {
                    token = getToken(tokens, ++offset);
                    if (!PunctuationToken.COMMA.equals(token)) {
                        return throwWrongSyntax(tokens, offset);
                    } else {
                        token = getToken(tokens, ++offset);
                    }
                } else {
                    break;
                }
            }
        }
        return sqlObject;
    }
}
