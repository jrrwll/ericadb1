package org.ericadb.first.sql.definition;

import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.sql.AbstractSqlObject;
import org.ericadb.first.store.DatabaseEngine;
import org.ericadb.first.store.StorageEngineManager;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
@Getter
@Setter
public class DescTableSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;

    public DescTableSqlObject(String sql, String tableName) {
        super(sql);
        this.tableName = tableName;
    }

    @Override
    public ResultObjects execute(SqlContext context) {
        DatabaseEngine databaseEngine = StorageEngineManager.getInstance().getDatabaseEngine();

        return databaseEngine.descTable(this, context);
    }
}