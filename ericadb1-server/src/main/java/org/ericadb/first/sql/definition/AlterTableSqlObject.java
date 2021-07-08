package org.ericadb.first.sql.definition;

import java.util.List;
import org.ericadb.first.common.result.Int32ResultObject;
import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.sql.AbstractSqlObject;
import org.ericadb.first.store.DatabaseEngine;
import org.ericadb.first.store.StorageEngineManager;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
public class AlterTableSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;
    List<ColumnDefinition> addColumnDefinitions;
    List<ColumnDefinition> modifyColumnDefinitions;
    List<String> dropColumns;

    public AlterTableSqlObject(String sql, String tableName) {
        super(sql);
        this.tableName = tableName;
    }

    @Override
    public ResultObjects execute(SqlContext context) {
        DatabaseEngine databaseEngine = StorageEngineManager.getInstance().getDatabaseEngine();

        boolean effect = databaseEngine.alterTable(this, context);

        if (effect) {
            return Int32ResultObject.ONE;
        } else {
            return Int32ResultObject.ZERO;
        }
    }
}