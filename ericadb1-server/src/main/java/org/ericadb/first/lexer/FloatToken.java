package org.ericadb.first.lexer;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
public class FloatToken extends AbsLexToken {

    final double value;

    public FloatToken(String rawString) {
        super(Type.LITERAL_FLOAT, rawString);
        this.value = Double.parseDouble(rawString);
    }

    @Override
    public boolean isLiteralFloat() {
        return true;
    }

    @Override
    public double getFloat() {
        return value;
    }
}