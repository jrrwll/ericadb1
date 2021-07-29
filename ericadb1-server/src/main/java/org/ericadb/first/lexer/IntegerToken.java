package org.ericadb.first.lexer;

import org.ericadb.first.common.result.Int64ResultObject;
import org.ericadb.first.common.result.ResultObject;

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
    public boolean isInteger() {
        return true;
    }

    @Override
    public long getInteger() {
        return value;
    }

    @Override
    public boolean isValue() {
        return true;
    }

    @Override
    public ResultObject getValue() {
        return new Int64ResultObject(value);
    }
}