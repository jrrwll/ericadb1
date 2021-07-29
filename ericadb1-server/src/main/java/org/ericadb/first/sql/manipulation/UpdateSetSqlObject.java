package org.ericadb.first.sql.manipulation;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.dreamcat.common.Pair;
import org.ericadb.first.common.result.Int32ResultObject;
import org.ericadb.first.common.result.ResultObject;
import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.sql.AbstractSqlObject;
import org.ericadb.first.sql.query.WhereObject;
import org.ericadb.first.store.StorageEngineManager;
import org.ericadb.first.store.TableEngine;

/**
 * @author Jerry Will
 * @version 2021-07-22
 */
@Getter
@Setter
public class UpdateSetSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;
    List<Pair<String, ResultObject>> sets;
    WhereObject where;

    protected UpdateSetSqlObject(String sql) {
        super(sql);
        this.sets = new ArrayList<>();
    }

    @Override
    public ResultObjects execute(SqlContext context) {
        TableEngine tableEngine = StorageEngineManager.getInstance().getTableEngine();

        int rowEffect = tableEngine.updateSet(this, context);

        return ResultObjects.singleton(new Int32ResultObject(rowEffect));
    }
}
