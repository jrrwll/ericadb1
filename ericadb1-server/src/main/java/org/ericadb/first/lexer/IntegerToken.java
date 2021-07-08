package org.ericadb.first.lexer;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
public class IntegerToken extends AbsLexToken {

    final long value;

    public IntegerToken(String rawString) {
        super(Type.LITERAL_INTEGER, rawString);
        this.value = Long.parseLong(rawString);
    }

    @Override
    public boolean isLiteralInteger() {
        return true;
    }

    @Override
    public long getInteger() {
        return value;
    }
}