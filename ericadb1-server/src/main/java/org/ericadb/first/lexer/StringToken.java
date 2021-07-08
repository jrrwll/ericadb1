package org.ericadb.first.lexer;

import org.dreamcat.common.util.StringUtil;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
public class StringToken extends AbsLexToken {

    final String value;

    public StringToken(String rawValue) {
        super(Type.LITERAL_STRING, String.format("'%s'", rawValue));
        this.value = StringUtil.fromBackslash(rawValue);
    }

    @Override
    public boolean isLiteralString() {
        return true;
    }

    @Override
    public String getString() {
        return value;
    }
}
