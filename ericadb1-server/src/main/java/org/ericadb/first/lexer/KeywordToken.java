package org.ericadb.first.lexer;

import java.util.HashMap;
import java.util.Map;
import org.ericadb.first.common.type.EType;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
public class KeywordToken extends AbsLexToken {

    final String value;

    public KeywordToken(String rawString) {
        super(Type.KEYWORD, rawString);
        this.value = rawString.toLowerCase();
    }

    @Override
    public boolean isKeyword() {
        return true;
    }

    @Override
    public String getKeyword() {
        return value;
    }

    public static EType parseType(String keyword) {
        return keywordTypes.get(keyword);
    }

    private static final Map<String, EType> keywordTypes;

    // conf
    public static final String CREATE = "create";
    public static final String DATABASE = "database";
    public static final String OR = "or";
    public static final String REPLACE = "replace";
    public static final String IF = "if";
    public static final String NOT = "not";
    public static final String EXISTS = "exists";

    // def
    public static final String DROP = "drop";
    public static final String TABLE = "table";
    public static final String NULL = "null";
    public static final String DEFAULT = "default";
    public static final String PRIMARY = "primary";
    public static final String KEY = "key";
    public static final String UNIQUE = "unique";

    // type
    public static final String BOOL = "bool";
    public static final String BOOLEAN = "boolean";
    public static final String I8 = "i8";
    public static final String INT8 = "int8";
    public static final String I16 = "i16";
    public static final String INT16 = "int16";
    public static final String I32 = "i32";
    public static final String INT32 = "int32";
    public static final String I64 = "i64";
    public static final String INT64 = "int64";
    public static final String F32 = "f32";
    public static final String FLOAT32 = "float32";
    public static final String F64 = "f64";
    public static final String FLOAT64 = "float64";
    public static final String STRING = "string";
    public static final String TEXT = "text";
    public static final String BINARY = "binary";

    static {
        keywordTypes = new HashMap<>();

    }
}
