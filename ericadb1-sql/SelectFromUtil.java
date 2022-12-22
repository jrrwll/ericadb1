package org.dreamcat.common.sql;

import static org.dreamcat.common.sql.CreateTableUtil.assertThat;
import static org.dreamcat.common.sql.CreateTableUtil.flag_identifier;
import static org.dreamcat.common.sql.CreateTableUtil.flag_number;
import static org.dreamcat.common.sql.CreateTableUtil.flag_operator;
import static org.dreamcat.common.sql.CreateTableUtil.flag_punctuation;
import static org.dreamcat.common.sql.CreateTableUtil.flag_string;
import static org.dreamcat.common.sql.CreateTableUtil.getIdentifierLike;
import static org.dreamcat.common.sql.CreateTableUtil.getToken;
import static org.dreamcat.common.sql.CreateTableUtil.lex;
import static org.dreamcat.common.sql.CreateTableUtil.throwWrongSyntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import org.dreamcat.common.Pair;
import org.dreamcat.common.Triple;
import org.dreamcat.common.sql.ValueCommonDef.OpType;

/**
 * note that dialect is unsupported
 *
 * <pre><code>
 * select
 *      1, len('abc'), avg(1 + score) as score_avg
 * from t1 a
 * inner join t2 b on a.id = b.cid
 * where
 *      ( a.id in (1, 2, 3) and b.slat is not null )
 *      or
 *      ( b.code in ('1', '2', '3') and a.name like '%test%' )
 * </pre></code>
 *
 * @author Jerry Will
 * @version 2022-08-02
 */
public class SelectFromUtil {

    public static SelectFromCommonDef parse(String sql) {
        List<Pair<Integer, Object>> tokens = lex(sql);
        ListIterator<Pair<Integer, Object>> iter = tokens.listIterator();
        SelectFromCommonDef selectFrom = parse0(iter);
        while (iter.hasNext()) {
            assertThat(iter.next(), flag_punctuation, ";");
        }
        return selectFrom;
    }

    // handle union
    static SelectFromCommonDef parse0(ListIterator<Pair<Integer, Object>> iter) {
        SelectFromCommonDef selectFrom = parse1(iter);
        if (!iter.hasNext()) return selectFrom;

        List<SelectFromCommonDef> union = new ArrayList<>();
        union.add(selectFrom);

        Pair<Integer, Object> pair = iter.next();
        while (pair.first() == flag_identifier && pair.second().equals("union")) {
            union.add(parse1(iter));
            if (!iter.hasNext()) break;
            pair = iter.next();
        }
        iter.previous();

        if (union.size() == 1) {
            return selectFrom;
        } else {
            return SelectFromCommonDef.fromUnion(union);
        }
    }

    // not handle union
    static SelectFromCommonDef parse1(ListIterator<Pair<Integer, Object>> iter) {
        SelectFromCommonDef selectFrom = new SelectFromCommonDef();

        assertThat(iter.next(), flag_identifier, "select");
        Pair<Integer, Object> pair = iter.next();
        if (pair.first() == flag_identifier && pair.second().equals("distinct")) {
            selectFrom.setDistinct(true);
        } else {
            iter.previous();
        }

        for (; ; ) {
            selectFrom.getSelect().add(parseValue(iter));
            if (!iter.hasNext()) break; // only has select

            pair = iter.next();
            if (pair.first() == flag_punctuation) {
                if (pair.second().equals(",")) {
                    continue;
                } else if (pair.second().equals(";") || pair.second().equals(")")) {
                    iter.previous();
                    break;
                }
            } else if (pair.first() == flag_identifier) {
                if (pair.second().equals("from") || pair.second().equals("union")) {
                    iter.previous();
                    break;
                }
            }
            throwWrongSyntax(pair.second());
        }
        if (selectFrom.getSelect().isEmpty()) {
            throwWrongSyntax(iter.previous());
        }
        pair = iter.next();

        if (pair.first() == flag_punctuation || pair.second().equals("union")) {
            return selectFrom;
        }

        // parse from
        Triple<List<JoinCommonDef>, SelectFromCommonDef, String> from = parseFrom(iter);
        if (from.first() != null) {
            selectFrom.setFrom(from.first());
        } else {
            selectFrom.setSubQueryFrom(from.second());
            selectFrom.setSubQueryAlias(from.third());
        }
        if (!iter.hasNext()) return selectFrom;

        //
        pair = iter.next();
        if (pair.first() != flag_identifier) {
            throwWrongSyntax(pair.second());
        }
        String identifier = (String) pair.second();
        if (identifier.equals("where")) {
            selectFrom.setWhere(parseWhere(iter));
        }
        if (!iter.hasNext()) return selectFrom;

        if (identifier.equals("group")) {
            Pair<List<ValueCommonDef>, ValueCommonDef> groupBy = parseGroupBy(iter);
            selectFrom.setGroupBy(groupBy.first());
            selectFrom.setHaving(groupBy.second());
        }

        if (identifier.equals("order")) {
            selectFrom.setOrderBy(parseOrderBy(iter));
        }

        if (identifier.equals("limit")) {
            Pair<Long, Long> limit = parseLimit(iter);
            selectFrom.setLimit(limit.first());
            selectFrom.setOffset(limit.second());
        }
        return selectFrom;
    }

    static ValueCommonDef parseValue(ListIterator<Pair<Integer, Object>> iter) {
        ValueCommonDef root = null, parent = null, current = null;
        while (iter.hasNext()) {
            Pair<Integer, Object> pair = iter.next();
            int flag = pair.first();
            Object token = pair.second();

        }
        return root;
    }

    // case: sum(a) > avg(a) * 3 + b, no `and/or/()`
    static ValueCommonDef parseOneValue(ListIterator<Pair<Integer, Object>> iter) {
        ValueCommonDef root = null, parent = null, current = null;
        while (iter.hasNext()) {
            Pair<Integer, Object> pair = iter.next();
            int flag = pair.first();
            Object token = pair.second();
            // dialect is unsupported
            if (flag == flag_identifier) {
                String identifier = (String) token;
                if (identifier.equals("and") || identifier.equals("or")) {
                    iter.previous();
                    return root;
                }
                boolean rootEqCurrent = root == current;
                if (current != null) {
                    // current is identifier
                    if (current.isIdentifier() || current.isConstant()) {
                        current = parseOneValueIdentifier(identifier, current, iter);
                    } else {
                        throwWrongSyntax(identifier);
                    }
                } else if (parent != null) {
                    if (parent.isOperator()) {
                        parent.getChildren().add(ValueCommonDef.ofIdentifier(identifier));
                        current = parent;
                        parent = current.getParent();
                    }
                } else {
                    current = ValueCommonDef.ofIdentifier(identifier);
                }

                if (rootEqCurrent) {
                    root = current;
                }
            } else if (flag == flag_string || flag == flag_number) {
                if (current == null) {
                    current = ValueCommonDef.ofConstant(token);
                } else {

                }
                if (root == null) {
                    root = current;
                }
            } else if (flag == flag_operator) {
                OpType op = OpType.ofOperator((String) token);
                if (current != null) {
                    if (current.isIdentifier() || current.isConstant()) {
                        ValueCommonDef newNode = new ValueCommonDef(op);
                        newNode.join(parent, current);
                        current = newNode;
                    }

                    if (parent == null) {
                        root = current;
                    }
                }
            }
        }
        return root;
    }

    private static ValueCommonDef parseOneValueIdentifier(String identifier,
            ValueCommonDef current,
            ListIterator<Pair<Integer, Object>> iter) {
        if (identifier.equals("is")) {
            String next = getToken(iter.next(), flag_identifier);
            // cc is null
            if (next.equals("null")) {
                return new ValueCommonDef(OpType.IS_NULL, current);
            }
            // cc is not null
            else if (next.equals("not")) {
                assertThat(iter.next(), flag_identifier, "null");
                return new ValueCommonDef(OpType.IS_NOT_NULL, current);
            } else {
                throwWrongSyntax(next);
            }
        }

        boolean not = false;
        if (identifier.equals("not")) {
            not = true;
            identifier = getToken(iter.next(), flag_identifier);
        }

        // cc [not] like 'xx'
        if (identifier.equals("like")) {
            String like = getToken(iter.next(), flag_string);
            return new ValueCommonDef(not ? OpType.NOT_LIKE : OpType.LIKE, current,
                    ValueCommonDef.ofConstant(like));
        }
        // cc [not] in ('xx')
        else if (identifier.equals("in")) {
            List<Object> in = parseInConstantList(iter);
            return new ValueCommonDef(not ? OpType.NOT_IN: OpType.IN, current,
                    ValueCommonDef.ofConstant(in));
        }
        // cc [not] between low and high
        else if (identifier.equals("between")) {
            Object low = getToken(iter.next(), flag_string, flag_number);
            assertThat(iter.next(), flag_identifier, "and");
            Object high = getToken(iter.next(), flag_string, flag_number);
            return new ValueCommonDef(not ? OpType.NOT_BETWEEN_AND : OpType.BETWEEN_AND, current,
                    ValueCommonDef.ofConstant(low), ValueCommonDef.ofConstant(high));
        } else {
            throwWrongSyntax(identifier);
        }
        return null;
    }

    static List<Object> parseInConstantList(ListIterator<Pair<Integer, Object>> iter) {
        assertThat(iter.next(), flag_identifier, "(");
        Pair<Integer, Object> pair = iter.next();
        int flag = pair.first();
        Object token = pair.second();
        if (flag == flag_punctuation && token.equals(")")) {
            return Collections.emptyList();
        }

        List<Object> in = new ArrayList<>();
        for (;;) {
            if (flag == flag_string || flag == flag_number) {
                in.add(token);
            } else {
                throwWrongSyntax(token);
            }
            pair = iter.next();
            flag = pair.first();
            token = pair.second();

            if (flag == flag_punctuation) {
                if (token.equals(")")) break;
                else if (!token.equals(",")) {
                    throwWrongSyntax(token);
                }
            }
        }
        return in;
    }

    static Triple<List<JoinCommonDef>, SelectFromCommonDef, String> parseFrom(
            ListIterator<Pair<Integer, Object>> iter) {
        assertThat(iter.next(), flag_identifier, "from");
        Pair<Integer, Object> pair = iter.next();
        if (pair.first() == flag_punctuation) {
            if (pair.second().equals("(")) {
                SelectFromCommonDef selectFrom = parse0(iter);
                assertThat(iter.next(), flag_punctuation, ")");
                String alias = getIdentifierLike(iter.next());
                return Triple.of(null, selectFrom, alias);
            }
            throwWrongSyntax(pair.second());
        }
        List<JoinCommonDef> joins = new ArrayList<>();
        // todo impl
        return Triple.of(joins, null, null);
    }

    // where xxx
    public static ValueCommonDef parseWhere(ListIterator<Pair<Integer, Object>> iter) {
        assertThat(iter.next(), flag_identifier, "where");
        return parseValue(iter);
    }

    // group by xxx [having xxx]
    public static Pair<List<ValueCommonDef>, ValueCommonDef> parseGroupBy(ListIterator<Pair<Integer, Object>> iter) {
        assertThat(iter.next(), flag_identifier, "group");
        assertThat(iter.next(), flag_identifier, "by");
        List<ValueCommonDef> groupBy = new ArrayList<>();
        for (; ; ) {
            groupBy.add(parseValue(iter));
            if (!iter.hasNext()) {
                return Pair.of(groupBy, null); // hit end
            }
            Pair<Integer, Object> pair = iter.next();
            if (pair.first() != flag_punctuation) {
                iter.previous();
                break; // hit edge
            }
            if (pair.second().equals(",")) continue; // continue group by
            if (pair.second().equals(";")) {
                return Pair.of(groupBy, null); // hit end
            }
            throwWrongSyntax(pair.second());
        }
        ValueCommonDef having = null;
        Pair<Integer, Object> pair = iter.next();
        if (pair.first() == flag_identifier && pair.second().equals("having")) {
            having = parseValue(iter);
        }

        return Pair.of(groupBy, having);
    }

    // order by a, b desc
    public static List<OrderByCommonDef> parseOrderBy(ListIterator<Pair<Integer, Object>> iter) {
        assertThat(iter.next(), flag_identifier, "order");
        assertThat(iter.next(), flag_identifier, "by");
        List<OrderByCommonDef> orderBy = new ArrayList<>();
        for (; ; ) {
            OrderByCommonDef order = new OrderByCommonDef();
            order.setValue(parseValue(iter));
            if (!iter.hasNext()) break;
            Pair<Integer, Object> pair = iter.next();
            if (pair.first() == flag_identifier) {
                if (pair.second().equals("desc")) {
                    order.setDesc(true);
                } else if (!pair.second().equals("asc")) {
                    break;
                }
                if (!iter.hasNext()) break;
                pair = iter.next();
            }

            if (pair.first() != flag_punctuation) {
                iter.previous();
                break; // hit edge
            }

            if (pair.second().equals(",")) continue; // continue order by
            if (pair.second().equals(";")) break;
            throwWrongSyntax(pair.second());
        }
        return orderBy;
    }

    // return limit & offset: limit x offset y, limit y, x
    public static Pair<Long, Long> parseLimit(ListIterator<Pair<Integer, Object>> iter) {
        assertThat(iter.next(), flag_identifier, "limit");
        Number a = getToken(iter.next(), flag_number);
        if (!iter.hasNext()) {
            // limit x
            return Pair.of(a.longValue(), null);
        }
        Pair<Integer, Object> pair = iter.next();
        if (pair.first() == flag_punctuation) {
            if (pair.second().equals(",")) {
                // limit y, x
                Number b = getToken(iter.next(), flag_number);
                return Pair.of(b.longValue(), a.longValue());
            }
        } else if (pair.first() == flag_identifier) {
            if (pair.second().equals("offset")) {
                // limit x offset y
                Number b = getToken(iter.next(), flag_number);
                return Pair.of(a.longValue(), b.longValue());
            }
        }
        iter.previous();
        return Pair.of(a.longValue(), null);
    }
}
