package org.ericadb.first.sql.manipulation;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.common.result.Int32ResultObject;
import org.ericadb.first.common.result.ResultObject;
import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.sql.AbstractSqlObject;
import org.ericadb.first.store.StorageEngineManager;
import org.ericadb.first.store.TableEngine;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
@Getter
@Setter
public class InsertIntoSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;
    // null means all columns
    List<String> columnNames;
    // values
    List<ResultObject[]> values;

    public InsertIntoSqlObject(String sql) {
        super(sql);
        this.values = new ArrayList<>();
    }

    @Override
    public ResultObjects execute(SqlContext context) {
        TableEngine tableEngine = StorageEngineManager.getInstance().getTableEngine();

        int rowEffect = tableEngine.insertInto(this, context);

        return ResultObjects.singleton(new Int32ResultObject(rowEffect));
    }
}
