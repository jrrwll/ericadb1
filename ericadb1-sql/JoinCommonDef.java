package org.dreamcat.common.sql;

import lombok.Data;

/**
 * @author Jerry Will
 * @version 2022-08-02
 */
@Data
public class JoinCommonDef {

    private String tableName;
    private String alias;
    private JoinType joinType;
    private JoinCommonDef on;

    public enum JoinType {
        INNER,
        LEFT,
        RIGHT,
        CROSS,
        FULL, // same as full outer
        ;
    }
}
