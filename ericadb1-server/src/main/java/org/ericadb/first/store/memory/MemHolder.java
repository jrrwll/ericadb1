package org.ericadb.first.store.memory;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.dreamcat.common.x.bean.BeanCopierUtil;
import org.ericadb.first.common.result.BinaryResultObject;
import org.ericadb.first.common.result.BoolResultObject;
import org.ericadb.first.common.result.Int32ResultObject;
import org.ericadb.first.common.result.Int8ResultObject;
import org.ericadb.first.common.result.ResultObject;
import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.common.result.TextResultObject;
import org.ericadb.first.common.type.EType;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.context.Tuple;
import org.ericadb.first.exception.EricaException;
import org.ericadb.first.sql.definition.ColumnDefinition;
import org.ericadb.first.sql.definition.CreateTableSqlObject;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
class MemHolder {

    Map<String, DbObj> databases = new ConcurrentHashMap<>();

    @RequiredArgsConstructor
    static class DbObj {

        final String databaseName;
        Map<String, TableObj> tables = new ConcurrentHashMap<>();
    }

    static class TableObj {

        List<String> primaryColumnNames;
        Map<String, ColumnObj> meta = new LinkedHashMap<>();
        // id  -->  one row data
        Map<Tuple, Object[]> rows = new LinkedHashMap<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ColumnObj {

        int index;
        String columnName;
        EType type;
        boolean notNull;
        ResultObject defaultValue;
    }

    static TableObj ofTableObj(CreateTableSqlObject sqlObject) {
        TableObj tableObj = new TableObj();
        tableObj.primaryColumnNames = sqlObject.getPrimaryColumnNames();
        List<ColumnDefinition> cds = sqlObject.getColumnDefinitions();
        for (ColumnDefinition cd : cds) {
            ColumnObj columnObj = BeanCopierUtil.copy(cd, ColumnObj.class);
            tableObj.meta.put(cd.getColumnName(), columnObj);
        }

        return tableObj;
    }

    static ResultObjects toResultObjects(TableObj tableObj) throws IOException {
        Collection<ColumnObj> columnObjs = tableObj.meta.values();
        ResultObject[][] body = new ResultObject[columnObjs.size()][];
        int rowOffset = 0;
        for (ColumnObj value : columnObjs) {
            body[rowOffset++] = new ResultObject[]{
                    new Int32ResultObject(value.index),
                    new TextResultObject(value.columnName),
                    new Int8ResultObject((byte) value.type.ordinal()),
                    new BoolResultObject(!value.notNull),
                    new BinaryResultObject(value.defaultValue),
            };

        }
        return new ResultObjects(DESC_TABLE_HEAD, body);
    }

    static final String[] DESC_TABLE_HEAD = new String[]{
            "index", "columnName", "type", "nullable", "default"
    };

    DbObj getDatabase(String databaseName, SqlContext context) {
        if (databaseName == null) databaseName = context.getCurrentDatabase();

        DbObj dbObj = databases.get(databaseName);
        if (dbObj == null) {
            throw new EricaException("database doesn't exists: " + databaseName);
        }
        return dbObj;
    }

    TableObj getTable(String tableName, DbObj dbObj) {
        TableObj tableObj = dbObj.tables.get(tableName);
        if (tableObj == null) {
            throw new EricaException("table doesn't exists: " + dbObj.databaseName + "." + tableName);
        }
        return tableObj;
    }
}
