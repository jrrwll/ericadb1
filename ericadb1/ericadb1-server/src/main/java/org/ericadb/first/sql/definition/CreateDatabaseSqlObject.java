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
 * @since 2021-07-03
 */
@Getter
@Setter
public class CreateDatabaseSqlObject extends AbstractSqlObject {

    String databaseName;
    boolean orReplace;
    boolean ifNotExists;

    public CreateDatabaseSqlObject(String sql, String databaseName, boolean orReplace, boolean ifNotExists) {
        super(sql);
        this.databaseName = databaseName;
        this.orReplace = orReplace;
        this.ifNotExists = ifNotExists;
    }

    @Override
    public ResultObjects execute(SqlContext context) {
        DatabaseEngine databaseEngine = StorageEngineManager.getInstance().getDatabaseEngine();

        boolean effect = databaseEngine.createDatabase(this, context);

        if (effect) {
            return Int32ResultObject.ONE;
        } else {
            return Int32ResultObject.ZERO;
        }
    }
}
