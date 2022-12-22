package org.dreamcat.common.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dreamcat.common.util.StringUtil;

/**
 * @author Jerry Will
 * @version 2022-08-02
 */
@Data
@NoArgsConstructor
public class ValueCommonDef {

    private OpType opType;
    /**
     * String, Number, List
     */
    private Object value;
    private ValueCommonDef parent;
    private List<ValueCommonDef> children;

    public static ValueCommonDef ofIdentifier(String identifier) {
        return of(OpType.IDENTIFIER, identifier);
    }

    public static ValueCommonDef ofConstant(Object constant) {
        return of(OpType.CONSTANT, constant);
    }

    private static ValueCommonDef of(OpType opType, Object value) {
        ValueCommonDef self = new ValueCommonDef();
        self.setOpType(opType);
        self.setValue(value);
        return self;
    }

    public ValueCommonDef(OpType opType, ValueCommonDef... children) {
        this.opType = opType;
        this.children = new ArrayList<>(Math.min(children.length, 2));
        Collections.addAll(this.children, children);
    }

    @Override
    public String toString() {
        if (value != null) {
            return value.toString();
        }
        int size = children.size();
        if (size == 1) {
            return opType.getAlias() + " " + children.get(0);
        } else if (size == 2) {
            return String.format("%s %s %s", children.get(0),
                    opType.getAlias(), children.get(1));
        }
        return String.format("%s(%s)", opType.getAlias(),
                StringUtil.join(children, ", "));
    }

    @Getter
    public enum OpType {
        IDENTIFIER("?"),
        CONSTANT("?"), // string, number, bool

        DOT("."),

        OR("||", "or"),
        AND("&&", "and"),

        EQ(true, "=", "=="),
        NE(true, "<>", "!="),
        GT(true, ">"),
        GE(true, ">="),
        LT(true, "<"),
        LE(true, "<="),
        ADD(true, "+"),
        SUB(true, "-"),
        MUL(true, "*"),
        DIV(true, "/"),
        REM(true, "%"),

        IS_NULL("is null"),
        IS_NOT_NULL("is not null"),
        LIKE("like"),
        NOT_LIKE("not like"),
        IN("in"),
        NOT_IN("not in"),
        BETWEEN_AND("between and"),
        NOT_BETWEEN_AND("not between and"),
        ;

        private final List<String> aliases;
        private final boolean operator;

        OpType(String... aliases) {
            this(false, aliases);
        }

        OpType(boolean operator, String... aliases) {
            this.operator = operator;
            this.aliases = Arrays.asList(aliases);
        }
        public String getAlias() {
            return aliases.get(0);
        }

        public static OpType ofOperator(String operator) {
            for (OpType opType : values()) {
                if (opType.isOperator() && opType.getAlias().equals(operator)) {
                    return opType;
                }
            }
            return null;
        }
    }

    public boolean isIdentifier() {
        return opType.equals(OpType.IDENTIFIER);
    }

    public boolean isConstant() {
        return opType.equals(OpType.CONSTANT);
    }

    public boolean isOperator() {
        return opType.operator;
    }

    void join(ValueCommonDef parent, ValueCommonDef child) {
        if (parent != null) {
            parent.removeChild(child);
            parent.addChild(this);
        }
        this.addChild(child);
    }

    void addChild(ValueCommonDef child) {
        children.add(child);
        child.parent = this;
    }

    void removeChild(ValueCommonDef child) {
        children.remove(child);
        child.parent = null;
    }
}
