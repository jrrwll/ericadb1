package org.ericadb.first.sql.definition;

import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
public class DropTableSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;
    boolean ifExists;

    public DropTableSqlObject(String sql, String tableName, boolean ifExists) {
        super(sql);
        this.tableName = tableName;
        this.ifExists = ifExists;
    }

    @Override
    public ResultObjects execute(SqlContext context) {
        DatabaseEngine databaseEngine = StorageEngineManager.getInstance().getDatabaseEngine();

        boolean effect = databaseEngine.dropTable(this, context);

        if (effect) {
            return Int32ResultObject.ONE;
        } else {
            return Int32ResultObject.ZERO;
        }
    }
}