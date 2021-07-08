package org.ericadb.first.lexer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dreamcat.common.Pair;
import org.ericadb.first.exception.EricaException;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
@Getter
@RequiredArgsConstructor
public enum OperatorToken implements LexToken {
    ADD("+"),
    MINUS("-"),
    MUL("*"),
    DIV("/"),
    MOD("%"),
    EQ("="),
    NE("<>"),
    GT(">"),
    GE(">="),
    LT("<"),
    LE("<="),
    ;

    final String rawToken;

    @Override
    public Type getType() {
        return Type.OPERATOR;
    }

    public static boolean isOperatorChar(char c) {
        return c == '+'
                || c == '-'
                || c == '*'
                || c == '/'
                || c == '%'
                || c == '='
                || c == '>'
                || c == '<';
    }

    public static Pair<OperatorToken, Integer> search(String sql, int offset) {
        char c = sql.charAt(offset);
        int size = sql.length();
        if (c == '+') {
            if (offset < size - 1 && isOperatorChar(sql.charAt(offset + 1))) {
                throwWrongSyntax(offset + 1);
            }
            return Pair.of(ADD, offset + 1);
        } else if (c == '-') {
            if (offset < size - 1 && isOperatorChar(sql.charAt(offset + 1))) {
                throwWrongSyntax(offset + 1);
            }
            return Pair.of(MINUS, offset + 1);
        } else if (c == '*') {
            if (offset < size - 1 && isOperatorChar(sql.charAt(offset + 1))) {
                throwWrongSyntax(offset + 1);
            }
            return Pair.of(MUL, offset + 1);
        } else if (c == '/') {
            if (offset < size - 1 && isOperatorChar(sql.charAt(offset + 1))) {
                throwWrongSyntax(offset + 1);
            }
            return Pair.of(DIV, offset + 1);
        } else if (c == '%') {
            if (offset < size - 1 && isOperatorChar(sql.charAt(offset + 1))) {
                throwWrongSyntax(offset + 1);
            }
            return Pair.of(MOD, offset + 1);
        } else if (c == '=') {
            char next;
            if (offset < size - 1 && isOperatorChar((next = sql.charAt(offset + 1)))
                    && next != '=') {
                throwWrongSyntax(offset + 1);
            }
            return Pair.of(EQ, offset + 1);
        } else if (c == '<') {
            char next = '\0';
            if (offset < size - 1 && isOperatorChar((next = sql.charAt(offset + 1)))
                    && (next != '=' && next != '>')) {
                throwWrongSyntax(offset + 1);
            }
            if (next == '=') {
                return Pair.of(LE, offset + 2);
            } else if (next == '>') {
                return Pair.of(NE, offset + 2);
            } else {
                return Pair.of(LT, offset + 1);
            }
        } else if (c == '>') {
            char next = '\0';
            if (offset < size - 1 && isOperatorChar((next = sql.charAt(offset + 1)))
                    && next != '=') {
                throwWrongSyntax(offset + 1);
            }
            if (next == '=') {
                return Pair.of(GE, offset + 2);
            } else {
                return Pair.of(GT, offset + 1);
            }
        } else {
            return null;
        }
    }

    private static void throwWrongSyntax(int i) {
        throw new EricaException(
                "You has wrong syntax in your SQL, near at " + i);
    }
}
