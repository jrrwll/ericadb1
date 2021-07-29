package org.ericadb.first.lexer;

import java.util.ArrayList;
import java.util.List;
import org.dreamcat.common.Pair;
import org.dreamcat.common.text.NumberSearcher;
import org.dreamcat.common.text.StringSearcher;
import org.dreamcat.common.util.StringUtil;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
public class SqlLexer {

    public LexTokens lex(String sql) {
        List<LexToken> tokens = new ArrayList<>();

        int size = sql.length();
        for (int i = 0; i < size; i++) {
            char c = sql.charAt(i);

            if (c <= ' ') continue;

            // keyword
            if (StringUtil.isFirstVariableChar(c)) {
                String v = StringSearcher.searchVariable(sql, i);
                tokens.add(new KeywordToken(v));
                i += v.length() - 1;
                continue;
            }

            // string
            if (c == '\'' || c == '`') {
                String value = StringSearcher.searchLiteralString(sql, i);
                if (value == null) {
                    throwWrongSyntax(size - 1);
                }
                tokens.add(new StringToken(value));
                i += value.length() + 1;
                continue;
            }

            // number
            if (StringUtil.isNumberChar(c)) {
                Pair<Integer, Boolean> pair = NumberSearcher.search(sql, i);
                if (pair == null) {
                    throwWrongSyntax(size - 1);
                }
                String value = sql.substring(i, pair.first());
                if (pair.second()) {
                    tokens.add(new FloatToken(value));
                } else {
                    tokens.add(new IntegerToken(value));
                }
                i += value.length() - 1;
                continue;
            }

            PunctuationToken punctuationToken = PunctuationToken.search(c);
            if (punctuationToken != null) {
                tokens.add(punctuationToken);
                continue;
            } else {
                // operator
                Pair<OperatorToken, Integer> pair = OperatorToken.search(sql, i);
                if (pair != null) {
                    tokens.add(pair.first());
                    i = pair.second();
                    continue;
                }
            }

            throwWrongSyntax(i);
        }

        return new LexTokens(tokens, sql);
    }

    private void throwWrongSyntax(int i) {
        throw new IllegalArgumentException(
                "You has wrong syntax in your SQL, near at " + i);
    }
}
