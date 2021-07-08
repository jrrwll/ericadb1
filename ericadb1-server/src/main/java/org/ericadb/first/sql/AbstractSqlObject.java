package org.ericadb.first.sql;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
@Getter
@Setter
public abstract class AbstractSqlObject implements SqlObject {

    protected String sql;

    protected AbstractSqlObject(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return sql;
    }
}
