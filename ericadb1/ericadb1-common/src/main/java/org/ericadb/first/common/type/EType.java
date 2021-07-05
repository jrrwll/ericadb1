package org.ericadb.first.common.type;

import java.io.IOException;
import java.io.InputStream;
import java.sql.JDBCType;
import org.dreamcat.common.function.ExpFunction;
import org.ericadb.first.common.result.BinaryResultObject;
import org.ericadb.first.common.result.BoolResultObject;
import org.ericadb.first.common.result.Float32ResultObject;
import org.ericadb.first.common.result.Float64ResultObject;
import org.ericadb.first.common.result.Int16ResultObject;
import org.ericadb.first.common.result.Int32ResultObject;
import org.ericadb.first.common.result.Int64ResultObject;
import org.ericadb.first.common.result.Int8ResultObject;
import org.ericadb.first.common.result.ResultObject;
import org.ericadb.first.common.result.TextResultObject;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
public enum EType {

    // null
    NULL(JDBCType.NULL, Void.class, ResultObject.NULL_READER,
            "null"),

    // boolean
    BOOL(JDBCType.BOOLEAN, Boolean.class, BoolResultObject::readFrom,
            "bool", "boolean"),

    // integer
    INT8(JDBCType.TINYINT, Byte.class, Int8ResultObject::readFrom,
            "i8", "int8", "tinyint"),
    INT16(JDBCType.SMALLINT, Short.class, Int16ResultObject::readFrom,
            "i16", "int16", "smallint"),
    INT32(JDBCType.INTEGER, Integer.class, Int32ResultObject::readFrom,
            "i32", "int32", "int"),
    INT64(JDBCType.BIGINT, Long.class, Int64ResultObject::readFrom,
            "i64", "int64", "bigint"),

    // float
    FLOAT32(JDBCType.FLOAT, Float.class, Float32ResultObject::readFrom,
            "f32", "float32", "float"),
    FLOAT64(JDBCType.DOUBLE, Double.class, Float64ResultObject::readFrom,
            "f64", "float64", "double"),

    // text
    TEXT(JDBCType.VARCHAR, String.class, TextResultObject::readFrom,
            "string", "text"),

    // binary
    BINARY(JDBCType.BINARY, byte[].class, BinaryResultObject::readFrom,
            "binary"),
    ;

    final JDBCType jdbcType;
    final Class<?> javaType;
    final ExpFunction<InputStream, ResultObject, IOException> reader;
    final String[] alias;

    EType(JDBCType jdbcType, Class<?> javaType,
            ExpFunction<InputStream, ResultObject, IOException> reader, String... alias) {
        this.jdbcType = jdbcType;
        this.javaType = javaType;
        this.reader = reader;
        this.alias = alias;
    }

    public ResultObject readFrom(InputStream input) throws IOException {
        return reader.apply(input);
    }
}
