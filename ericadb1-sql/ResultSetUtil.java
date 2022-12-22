package org.dreamcat.common.sql;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.dreamcat.common.util.ReflectUtil;

/**
 * Create by tuke on 2019-01-23
 */
public final class ResultSetUtil {

    private ResultSetUtil() {
    }

    public static <T> T map(ResultSet rs, Class<T> clazz) throws SQLException {
        T bean = ReflectUtil.newInstance(clazz);
        map(rs, bean);
        return bean;
    }

    public static <T> void map(ResultSet rs, T bean) throws SQLException {
        Class<?> clazz = Objects.requireNonNull(bean).getClass();
        List<Field> fieldList = ReflectUtil.retrieveFields(clazz);
        int size = fieldList.size();
        for (int i = 1; i <= size; i++) {
            Field field = fieldList.get(i);
            Class<?> fieldClass = field.getType();
            Object value = getByClass(rs, fieldClass, i);

            ReflectUtil.setValue(bean, field, value);
        }
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static List<Object> getRow(ResultSet rs, int columnSize) throws SQLException {
        List<Object> list = new ArrayList<>(columnSize);
        for (int i = 1; i <= columnSize; i++) {
            list.add(rs.getObject(i));
        }
        return list;
    }

    public static List<List<Object>> batchGetRow(ResultSet rs, int columnSize, int batchNumber)
            throws SQLException {
        List<List<Object>> a = new ArrayList<>(batchNumber);
        for (int i = 0; i < batchNumber; i++) {
            if (!rs.next()) break;
            a.add(getRow(rs, columnSize));
        }
        return a;
    }

    public static List<List<Object>> getAll(ResultSet rs, int columnSize) throws SQLException {
        List<List<Object>> rows = new ArrayList<>();
        while (rs.next()) {
            rows.add(getRow(rs, columnSize));
        }
        return rows;
    }

    public static String toInsertSql(List<Object[]> batchArgs, String table) {
        return toInsertSql(batchArgs, table, "*");
    }

    public static String toInsertSql(List<Object[]> batchArgs, String table, String columnNames) {
        StringBuilder sql = new StringBuilder(String.format(
                "insert into `%s`(%s) values ", table, columnNames));

        int size = batchArgs.size();
        for (int i = 0; i < size; i++) {
            Object[] row = batchArgs.get(i);
            sql.append("(");
            sql.append(Arrays.stream(row).map(it -> {
                if (it == null) return "NULL";
                else if (it instanceof Boolean) {
                    return (Boolean) it ? "1" : "0";
                } else {
                    return String.format("'%s'", escape(it.toString()));
                }
            }).collect(Collectors.joining(",")));
            sql.append(")");
            if (i != size - 1) sql.append(",");
        }
        sql.append(";");
        return sql.toString();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static Object getByClass(ResultSet rs, Class<?> clazz, int columnIndex)
            throws SQLException {
        if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return rs.getLong(columnIndex);
        }

        if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return rs.getInt(columnIndex);
        }

        if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return rs.getDouble(columnIndex);
        }

        if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            return rs.getFloat(columnIndex);
        }

        if (clazz.equals(Date.class)) {
            return rs.getDate(columnIndex);
        }

        return rs.getString(columnIndex);
    }

    // escape to SQL Literal, only handle ' and /
    private static String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("'", "\\'");
    }
}
