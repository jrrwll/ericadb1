package org.ericadb.first.sql.manipulation;

import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.sql.AbstractSqlObject;
import org.ericadb.first.sql.query.WhereObject;
import org.ericadb.first.syntax.aware.DatabaseTableNameAware;

/**
 * @author Jerry Will
 * @version 2021-07-22
 */
@Getter
@Setter
public class DeleteFromSqlObject extends AbstractSqlObject implements DatabaseTableNameAware {

    String databaseName;
    String tableName;
    WhereObject where;

    protected DeleteFromSqlObject(String sql) {
        super(sql);
    }

    @Override
    public ResultObjects execute(SqlContext context) {
        return null;
    }
}
