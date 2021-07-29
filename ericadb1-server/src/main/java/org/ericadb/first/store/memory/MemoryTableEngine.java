package org.ericadb.first.store.memory;

import java.util.ArrayList;
import java.util.List;
import org.ericadb.first.common.result.ResultObject;
import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.context.Tuple;
import org.ericadb.first.exception.EricaException;
import org.ericadb.first.sql.manipulation.DeleteFromSqlObject;
import org.ericadb.first.sql.manipulation.InsertIntoSqlObject;
import org.ericadb.first.sql.manipulation.UpdateSetSqlObject;
import org.ericadb.first.sql.query.SelectFromSqlObject;
import org.ericadb.first.store.TableEngine;
import org.ericadb.first.store.memory.MemHolder.ColumnObj;
import org.ericadb.first.store.memory.MemHolder.DbObj;
import org.ericadb.first.store.memory.MemHolder.TableObj;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
public class MemoryTableEngine implements TableEngine {

    public static TableEngine getInstance() {
        return INSTANCE;
    }

    static final TableEngine INSTANCE = new MemoryTableEngine();

    MemHolder memHolder = (MemHolder) MemoryDatabaseEngine.getInstance();

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @Override
    public int insertInto(InsertIntoSqlObject sqlObject, SqlContext context) {
        String databaseName = sqlObject.getDatabaseName();
        String tableName = sqlObject.getTableName();
        List<String> columnNames = sqlObject.getColumnNames();
        List<ResultObject[]> values = sqlObject.getValues();

        DbObj dbObj = memHolder.getDatabase(databaseName, context);
        TableObj tableObj = memHolder.getTable(tableName, dbObj);

        List<String> allColumnNames = new ArrayList<>(tableObj.meta.keySet());
        List<String> primaryColumnNames = tableObj.primaryColumnNames;
        if (columnNames == null) {
            columnNames = allColumnNames;
            if (values.get(0).length != columnNames.size()) {
                throw new EricaException("insert columns count is not matched the values");
            }
        }

        int size = columnNames.size();
        int rowEffect = 0;
        for (ResultObject[] value : values) {

            List<String> insertColumnNames = new ArrayList<>();
            Object[] idValue = new Object[primaryColumnNames.size()];
            Object[] row = new Object[allColumnNames.size()];
            tableObj.rows.put(Tuple.of(idValue), row);
            rowEffect++;
            for (int i = 0; i < size; i++) {
                ResultObject object = value[i];
                String columnName = columnNames.get(i);

                insertColumnNames.add(columnName);

                int pi = primaryColumnNames.indexOf(columnName);
                if (pi != -1) idValue[pi] = object;
                row[tableObj.meta.get(columnName).index - 1] = object;
            }

            List<String> missColumnNames = new ArrayList<>(allColumnNames);
            missColumnNames.removeAll(insertColumnNames);

            for (String missColumnName : missColumnNames) {
                ColumnObj columnObj = tableObj.meta.get(missColumnName);
                ResultObject defaultValue = columnObj.defaultValue;
                if (defaultValue == null) {
                    if (columnObj.notNull) {
                        throw new EricaException("column `" + missColumnName + "` maybe not be null");
                    }
                } else {
                    row[columnObj.index - 1] = defaultValue;
                }
            }

        }
        return rowEffect;
    }

    @Override
    public int updateSet(UpdateSetSqlObject sqlObject, SqlContext context) {
        String databaseName = sqlObject.getDatabaseName();
        String tableName = sqlObject.getTableName();

        DbObj dbObj = memHolder.getDatabase(databaseName, context);
        TableObj tableObj = memHolder.getTable(tableName, dbObj);
        // filter all rows
        tableObj.rows.forEach((k, v) -> {
            // todo
        });
        return 0;
    }

    @Override
    public int deleteFrom(DeleteFromSqlObject sqlObject, SqlContext context) {
        return 0;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @Override
    public ResultObjects selectFrom(SelectFromSqlObject sqlObject, SqlContext context) {
        return null;
    }
}
