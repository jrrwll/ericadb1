package org.ericadb.first.type;

import java.sql.JDBCType;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
public enum EType {

    // null
    NULL(JDBCType.NULL, Void.class, "null"),

    // boolean
    BOOL(JDBCType.BOOLEAN, Boolean.class, "bool", "boolean"),

    // integer
    INT8(JDBCType.TINYINT, Byte.class, "i8", "int8", "tinyint"),
    INT16(JDBCType.SMALLINT, Short.class, "i16", "int16", "smallint"),
    INT32(JDBCType.INTEGER, Integer.class, "i32", "int32", "int"),
    INT64(JDBCType.BIGINT, Long.class, "i64", "int64", "bigint"),

    // float
    FLOAT32(JDBCType.FLOAT, Float.class, "f32", "float32", "float"),
    FLOAT64(JDBCType.DOUBLE, Double.class, "f64", "float64", "double"),

    // text
    TEXT(JDBCType.VARCHAR, String.class, "string", "text"),

    // binary
    BINARY(JDBCType.BINARY, byte[].class, "binary"),
    ;

    final JDBCType jdbcType;
    final Class<?> javaType;
    final String[] alias;

    EType(JDBCType jdbcType, Class<?> javaType, String... alias) {
        this.jdbcType = jdbcType;
        this.javaType = javaType;
        this.alias = alias;
    }
}
