package org.ericadb.first.lexer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
@Getter
@RequiredArgsConstructor
public enum PunctuationToken implements LexToken {
    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")"),
    COMMA(",");

    final String rawToken;

    @Override
    public Type getType() {
        return Type.PUNCTUATION;
    }

    public static PunctuationToken search(char c) {
        if (c == '(') {
            return LEFT_PARENTHESIS;
        } else if (c == ')') {
            return RIGHT_PARENTHESIS;
        }
        if (c == ',') {
            return COMMA;
        } else {
            return null;
        }
    }
}
