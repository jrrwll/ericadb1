package org.ericadb.first.sql;

import lombok.Getter;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
@Getter
public abstract class AbstractSqlObject implements SqlObject {

    protected String sql;

    public AbstractSqlObject(String sql) {
        this.sql = sql;
    }

    @Override
    public final String toString() {
        return sql;
    }
}
