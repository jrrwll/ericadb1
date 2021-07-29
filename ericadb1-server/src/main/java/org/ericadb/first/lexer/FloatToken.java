package org.ericadb.first.lexer;

import org.ericadb.first.common.result.Float64ResultObject;
import org.ericadb.first.common.result.ResultObject;

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
    public boolean isFloat() {
        return true;
    }

    @Override
    public double getFloat() {
        return value;
    }

    @Override
    public boolean isValue() {
        return true;
    }

    @Override
    public ResultObject getValue() {
        return new Float64ResultObject(value);
    }
}