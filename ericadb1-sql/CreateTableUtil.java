package org.dreamcat.common.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import org.dreamcat.common.Pair;
import org.dreamcat.common.text.NumberSearcher;
import org.dreamcat.common.text.StringSearcher;
import org.dreamcat.common.util.NumberUtil;
import org.dreamcat.common.util.SetUtil;
import org.dreamcat.common.util.StringUtil;

/**
 * @author Jerry Will
 * @version 2022-07-16
 */
@SuppressWarnings({"unchecked"})
public class CreateTableUtil {

    public static List<TableCommonDef> parse(String sql) {
        List<Pair<Integer, Object>> tokens = lex(sql);
        ListIterator<Pair<Integer, Object>> iter = tokens.listIterator();
        List<TableCommonDef> tables = new ArrayList<>();
        try {
            while (iter.hasNext()) {
                tables.add(parse0(iter));
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("wrong sql syntax");
        }
        return tables;
    }

    private static TableCommonDef parse0(ListIterator<Pair<Integer, Object>> iter) {
        TableCommonDef table = new TableCommonDef();
        Pair<Integer, Object> pair;
        assertThat(iter.next(), flag_identifier, "create");
        Object token = getToken(iter.next(), flag_identifier);
        boolean orReplace = false;
        if (token.equals("or")) {
            assertThat(iter.next(), flag_identifier, "replace");
            orReplace = true;
            table.setOrReplace(true);
        }
        assertThat(token, "table");
        pair = iter.next();
        if (pair.first() == flag_identifier) {
            String identifier = (String) pair.second();
            if (identifier.equals("if")) {
                if (orReplace) throwWrongSyntax("if");
                assertThat(iter.next(), flag_identifier, "not");
                assertThat(iter.next(), flag_identifier, "exists");
                table.setIfNotExists(true);
                pair = iter.next();
                int flag = pair.first();
                if (flag == flag_identifier || flag == flag_backtick_string) {
                    table.setName((String) pair.second());
                } else throwWrongSyntax("if");
            } else {
                table.setName(identifier);
            }
        } else if (pair.first() == flag_backtick_string) {
            table.setName((String) pair.second());
        } else throwWrongSyntax(pair.second());

        assertThat(iter.next(), flag_punctuation, "(");

        // columns
        boolean closeCreate = false;
        List<ColumnCommonDef> columns = new ArrayList<>();
        table.setColumns(columns);
        IndexCommonDef primaryKey = null;
        List<IndexCommonDef> indexes = new ArrayList<>();
        table.setIndexes(indexes);
        List<IndexCommonDef> uniqueIndexes = new ArrayList<>();
        table.setUniqueIndexes(uniqueIndexes);
        List<ForeignKeyCommonDef> foreignKeys = new ArrayList<>();
        table.setForeignKeys(foreignKeys);

        col:
        for (; ; ) {
            String columnName;
            if ((pair = iter.next()).first() == flag_identifier) {
                columnName = (String) pair.second();
                if (index_prefixs.contains(columnName)) {
                    iter.previous();
                    break;
                }
            } else {
                columnName = getToken(pair, flag_backtick_string);
            }

            ColumnCommonDef column = new ColumnCommonDef();
            columns.add(column);
            column.setName(columnName);
            column.setType(getToken(iter.next(), flag_identifier));

            if ((pair = iter.next()).first() == flag_punctuation) {
                if (pair.second().equals(")")) {
                    closeCreate = true;
                    break;
                }
                assertThat(pair.second(), "(");
                List<Integer> typeParams = new ArrayList<>();
                typeParams.add(((Number) getToken(iter.next(), flag_number)).intValue());
                while ((pair = iter.next()).first() == flag_punctuation && pair.second().equals(",")) {
                    typeParams.add(((Number) getToken(iter.next(), flag_number)).intValue());
                }
                column.setTypeParams(typeParams);
                assertThat(pair, flag_punctuation, ")");
                pair = iter.next();
            }

            boolean unique = false, key = false;
            for (; ; ) {
                if (pair.first() == flag_punctuation) {
                    if (pair.second().equals(",")) {
                        continue col;
                    } else if (pair.second().equals(")")) {
                        closeCreate = true;
                        break col;
                    } else throwWrongSyntax(pair.second());
                }

                token = getToken(pair, flag_identifier);
                if (token.equals("default")) {
                    column.setDefaultValue(getExpression(iter.next()));
                } else if (token.equals("not")) {
                    assertThat(iter.next(), flag_identifier, "null");
                    column.setNotNull(true);
                } else if (token.equals("comment")) {
                    column.setComment(getToken(iter.next(), flag_string));
                } else if (token.equals("on")) {
                    assertThat(iter.next(), flag_identifier, "update");
                    column.setOnUpdate(getExpression(iter.next()));
                } else if (token.equals("auto_increment")) {
                    column.setAutoIncrement(true);
                } else if (token.equals("primary")) {
                    if (primaryKey != null) throwWrongSyntax(token);
                    assertThat(iter.next(), flag_identifier, "key");
                    primaryKey = new IndexCommonDef();
                    primaryKey.setColumns(Collections.singletonList(columnName));
                } else if (token.equals("unique")) {
                    if (!key && !unique) {
                        IndexCommonDef index = new IndexCommonDef();
                        index.setColumns(Collections.singletonList(columnName));
                        uniqueIndexes.add(index);
                        unique = true;
                    }
                } else if (token.equals("key")) {
                    if (!unique && !key) {
                        IndexCommonDef index = new IndexCommonDef();
                        index.setColumns(Collections.singletonList(columnName));
                        indexes.add(index);
                        key = true;
                    }
                } else if (token.equals("unsigned")) {
                    column.setUnsigned(true);
                } else if (!token.equals("null")) throwWrongSyntax(token);

                pair = iter.next();
            }
        }

        // index
        if (!closeCreate) {
            for (; ; ) {
                token = getToken(iter.next(), flag_identifier);
                if (token.equals("primary")) {
                    if (primaryKey != null) throwWrongSyntax(token);
                    assertThat(iter.next(), flag_identifier, "key");
                    primaryKey = getIndex(iter, false);
                } else if (token.equals("unique")) {
                    IndexCommonDef uniqueIndex = getIndex(iter, true);
                    uniqueIndexes.add(uniqueIndex);
                } else if (token.equals("index") || token.equals("key")) {
                    IndexCommonDef index = getIndex(iter, false);
                    indexes.add(index);
                } else if (token.equals("foreign")) {
                    assertThat(iter.next(), flag_identifier, "key");
                    IndexCommonDef index = getIndex(iter, false);
                    if (index.getColumns().size() != 1) {
                        throwWrongSyntax(iter.previous().getSecond());
                    }
                    assertThat(iter.next(), flag_identifier, "references");
                    IndexCommonDef references = getIndex(iter, false);
                    if (references.getColumns().size() != 1) {
                        throwWrongSyntax(iter.previous().getSecond());
                    }

                    ForeignKeyCommonDef foreignKey = new ForeignKeyCommonDef();
                    foreignKey.setName(index.getName());
                    foreignKey.setColumn(index.getColumns().get(0));
                    foreignKey.setReferenceTable(references.getName());
                    foreignKey.setReferenceColumn(references.getColumns().get(0));
                    foreignKeys.add(foreignKey);
                } else throwWrongSyntax(token);

                pair = iter.next();
                if (pair.first() == flag_punctuation) {
                    if (pair.second().equals(")")) break;
                    if (pair.second().equals(",")) continue;
                }
                throwWrongSyntax(pair.second());
            }
        }

        table.setPrimaryKey(primaryKey);

        // table properties
        List<String> rawProperties = new ArrayList<>();
        String comment = null;
        Boolean justComment = null;
        while (iter.hasNext()) {
            pair = iter.next();
            if (pair.first() == flag_punctuation) {
                if (pair.second().equals(";")) break;
            }

            String rawProperty;
            if (pair.first() == flag_string) {
                rawProperty = StringUtil.escape((String) pair.second(), '\'');
                if (Objects.equals(justComment, true)) {
                    comment = (String) pair.second();
                }
                rawProperties.add("'" + rawProperty + "'");
            } else {
                rawProperty = pair.second().toString();
                rawProperties.add(rawProperty);

                if (pair.first() == flag_identifier && justComment == null &&
                        rawProperty.equals("comment")) {
                    justComment = true;
                    continue;
                }
            }
            if (Objects.equals(justComment, true)) {
                if (pair.first() != flag_operator || !rawProperty.equals("=")) {
                    justComment = false; // only keep true if prev token is =
                }
            }
        }
        if (!rawProperties.isEmpty()) {
            table.setRawProperties(String.join(" ", rawProperties));
        }
        table.setComment(comment);
        return table;
    }

    private static IndexCommonDef getIndex(ListIterator<Pair<Integer, Object>> iter, boolean keyOrIndex) {
        Pair<Integer, Object> pair = iter.next();
        IndexCommonDef index = new IndexCommonDef();
        if (pair.first() == flag_punctuation) {
            iter.previous();
        } else if (pair.first() == flag_identifier) {
            String token = (String) pair.second();
            if (keyOrIndex && (token.equals("key") || token.equals("index"))) {
                pair = iter.next();
                if (pair.first() == flag_punctuation) {
                    iter.previous();
                } else {
                    index.setName(getStringLike(pair));
                }
            } else {
                index.setName(token);
            }
        } else {
            index.setName(getStringLike(pair));
        }
        assertThat(iter.next(), flag_punctuation, "(");
        index.setColumns(getCommaSepList(iter));
        return index;
    }

    private static List<String> getCommaSepList(ListIterator<Pair<Integer, Object>> iter) {
        List<String> names = new ArrayList<>();
        Pair<Integer, Object> pair;
        for (; ; ) {
            names.add(getStringLike(iter.next()));
            pair = iter.next();
            if (pair.first() == flag_punctuation) {
                if (pair.second().equals(")")) break;
                if (pair.second().equals(",")) continue;
            }
            throwWrongSyntax(pair.second());
        }
        return names;
    }

    static void assertThat(Pair<Integer, Object> pair, int flag, Object expect) {
        Object token = getToken(pair, flag);
        assertThat(token, expect);
    }

    static <T> T getToken(Pair<Integer, Object> pair, int... flags) {
        Object token = pair.second();
        int flag = pair.first();
        if (Arrays.stream(flags).noneMatch(i -> i == flag)) {
            throwWrongSyntax(token);
        }
        return (T) token;
    }

    static void assertThat(Object token, Object expect) {
        if (!Objects.equals(token, expect)) throwWrongSyntax(token);
    }

    static void throwWrongSyntax(Object token) {
        throw new IllegalArgumentException("wrong sql syntax, invalid token: " + token);
    }

    private static String getStringLike(Pair<Integer, Object> pair) {
        if (pair.first() != flag_string &&
                pair.first() != flag_backtick_string &&
                pair.first() != flag_identifier) throwWrongSyntax(pair.second());
        return (String) pair.second();
    }

    static String getIdentifierLike(Pair<Integer, Object> pair) {
        if (pair.first() != flag_backtick_string &&
                pair.first() != flag_identifier) throwWrongSyntax(pair.second());
        return (String) pair.second();
    }

    private static String getExpression(Pair<Integer, Object> pair) {
        if (pair.first() == flag_punctuation ||
                pair.first() == flag_operator) throwWrongSyntax(pair.second());
        if (pair.first() == flag_string) {
            return String.format("'%s'", StringUtil.escape((String) pair.second(), '\''));
        } else {
            return pair.second().toString();
        }
    }

    static List<Pair<Integer, Object>> lex(String sql) {
        List<Pair<Integer, Object>> tokens = new ArrayList<>();
        int size = sql.length();
        int line = 1, j;
        String s;
        for (int i = 0; i < size; i++) {
            char c = sql.charAt(i);
            if (c <= ' ') {
                if (c == '\n') line++;
            }
            // comment #
            else if (c == '#') {
                for (j = i + 1; j < size && (c = sql.charAt(j)) != '\n'; j++) ;
                i = j;
                if (c == '\n') {
                    line++; // found \n
                } else break; // found EOF
            }
            // comment --
            else if (c == '-' && i < size - 2 && sql.charAt(i + 1) == '-') {
                for (j = i + 2; j < size && (c = sql.charAt(j)) != '\n'; j++) ;
                i = j;
                if (c == '\n') line++; // found \n
                else break; // found EOF
            }
            // comment /* */
            else if (c == '/' && i < size - 2 && sql.charAt(i + 1) == '*') {
                for (j = i + 2; j < size - 1 && sql.charAt(j) != '*'
                        && sql.charAt(j + 1) != '/'; j++)
                    ;
                if (sql.charAt(j) != '*' && sql.charAt(j + 1) != '/') {
                    throw new IllegalArgumentException("unclosed comment /* */");
                }
                i = j + 1;
            }
            // identifier
            else if (StringUtil.isFirstVariableChar(c)) {
                String v = StringSearcher.searchVar(sql, i);
                assert v != null;
                tokens.add(Pair.of(flag_identifier, v.toLowerCase()));
                i += v.length() - 1;
            }
            // string
            else if (c == '\'' || c == '"' || c == '`') {
                String value = StringSearcher.searchLiteral(sql, i);
                assert value != null;
                tokens.add(Pair.of(c == '`' ? flag_backtick_string : flag_string, value));
                i += value.length() + 1;
            }
            // number
            else if (StringUtil.isNumberChar(c)) {
                Pair<Integer, Boolean> pair = NumberSearcher.search(sql, i);
                assert pair != null;
                String value = sql.substring(i, pair.first());
                Number num = NumberUtil.parseNumber(value, pair.second());
                tokens.add(Pair.of(flag_number, num));
                i += value.length() - 1;
            }
            // punctuation
            else if (punctuations.contains(s = String.valueOf(c))) {
                tokens.add(Pair.of(flag_punctuation, s));
            }
            // operator
            else if (operators.contains(s)) {
                tokens.add(Pair.of(flag_operator, s));
            } else throw new IllegalArgumentException("invalid char in line " + line);
        }
        return tokens;
    }

    private static final Set<String> index_prefixs = SetUtil.of(
            "primary", "key", "index", "unique", "foreign");
    private static final Set<String> punctuations = SetUtil.of(
            ",", ";", "(", ")", "[", "]", "{", "}");
    private static final Set<String> operators = SetUtil.of(
            "=", "<", ">", "!", "+", "-", "*", "/", "%");

    static final int flag_identifier = 1;
    static final int flag_backtick_string = 2;
    static final int flag_string = 3;
    static final int flag_number = 4;
    static final int flag_punctuation = 5;
    static final int flag_operator = 6;
}
