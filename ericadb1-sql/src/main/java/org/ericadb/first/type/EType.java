package org.ericadb.first.type;

import java.sql.JDBCType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    CHAR(JDBCType.CHAR, String.class, "char"),
    VARCHAR(JDBCType.VARCHAR, String.class, "varchar"),
    TEXT(JDBCType.LONGVARCHAR, String.class, "string", "text"),

    // binary
    BINARY(JDBCType.BINARY, byte[].class, "binary"),
    BIT(JDBCType.BIT, byte[].class, "bit"),

    // datetime
    TIMESTAMP(JDBCType.TIMESTAMP, Date.class, "timestamp"),
    DATE(JDBCType.DATE, LocalDate.class, "date"),
    TIME(JDBCType.TIME, LocalTime.class, "time"),
    ;

    final JDBCType jdbcType;
    final Class<?> javaType;
    final String[] alias;

    EType(JDBCType jdbcType, Class<?> javaType, String... alias) {
        this.jdbcType = jdbcType;
        this.javaType = javaType;
        this.alias = alias;
    }

    public static EType of(String alias) {
        return valueMap.get(alias);
    }

    private static final Map<String, EType> valueMap = new HashMap<>();

    static {
        for (EType type : values()) {
            for (String alias : type.alias) {
                valueMap.put(alias, type);

            }
        }
    }
}
