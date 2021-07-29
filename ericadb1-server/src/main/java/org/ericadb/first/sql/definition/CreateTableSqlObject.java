package org.ericadb.first.sql.definition;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.common.result.Int32ResultObject;
import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.sql.AbstractSqlObject;
import org.ericadb.first.store.DatabaseEngine;
import org.ericadb.first.store.StorageEngineManager;
import org.ericadb.first.syntax.aware.DatabaseTableNameAware;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
@Getter
@Setter
public class CreateTableSqlObject extends AbstractSqlObject implements DatabaseTableNameAware {

    String databaseName;
    String tableName;
    boolean orReplace;
    boolean ifNotExists;
    List<ColumnDefinition> columnDefinitions;
    List<String> primaryColumnNames;

    public CreateTableSqlObject(String sql) {
        super(sql);
        this.columnDefinitions = new ArrayList<>();
    }

    public CreateTableSqlObject(String sql, String databaseName, String tableName,
            boolean orReplace, boolean ifNotExists) {
        this(sql);
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.orReplace = orReplace;
        this.ifNotExists = ifNotExists;
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
