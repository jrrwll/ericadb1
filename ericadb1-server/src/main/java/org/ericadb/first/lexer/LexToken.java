package org.ericadb.first.lexer;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
public interface LexToken {

    String getRawToken();

    Type getType();

    default boolean isKeyword() {
        return false;
    }

    default String getKeyword() {
        return null;
    }

    default boolean isLiteralString() {
        return false;
    }

    default String getString() {
        return null;
    }

    default boolean isLiteralInteger() {
        return false;
    }

    default long getInteger() {
        return 0;
    }

    default boolean isLiteralFloat() {
        return false;
    }

    default double getFloat() {
        return 0;
    }

    default boolean isOperator() {
        return false;
    }

    default OperatorToken getOperator() {
        return null;
    }

    enum Type {
        KEYWORD,
        LITERAL_STRING,
        LITERAL_INTEGER,
        LITERAL_FLOAT,
        OPERATOR,
        PUNCTUATION,
    }
}