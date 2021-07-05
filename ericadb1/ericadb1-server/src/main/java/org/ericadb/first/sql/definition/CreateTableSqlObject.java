package org.ericadb.first.sql.definition;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
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
public class CreateTableSqlObject extends AbstractSqlObject {

    String tableName;
    boolean orReplace;
    boolean ifNotExists;
    List<ColumnDefinition> columnDefinitions;

    public CreateTableSqlObject(String sql, String tableName, boolean orReplace, boolean ifNotExists) {
        super(sql);
        this.tableName = tableName;
        this.orReplace = orReplace;
        this.ifNotExists = ifNotExists;
        this.columnDefinitions = new ArrayList<>();
    }

    @Override
    public ResultObjects execute(SqlContext context) {
        DatabaseEngine databaseEngine = StorageEngineManager.getInstance().getDatabaseEngine();

        boolean effect = databaseEngine.createTable(this, context);

        if (effect) {
            return Int32ResultObject.ONE;
        } else {
            return Int32ResultObject.ZERO;
        }
    }
}
