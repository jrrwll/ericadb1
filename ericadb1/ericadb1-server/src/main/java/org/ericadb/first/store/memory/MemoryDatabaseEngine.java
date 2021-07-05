package org.ericadb.first.store.memory;

import org.ericadb.first.context.SqlContext;
import org.ericadb.first.exception.EricaException;
import org.ericadb.first.sql.configuration.UseSqlObject;
import org.ericadb.first.sql.definition.CreateDatabaseSqlObject;
import org.ericadb.first.sql.definition.CreateTableSqlObject;
import org.ericadb.first.sql.definition.DropDatabaseSqlObject;
import org.ericadb.first.store.DatabaseEngine;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
public class MemoryDatabaseEngine extends MemHolder implements DatabaseEngine {

    public static DatabaseEngine getInstance() {
        return INSTANCE;
    }

    static final DatabaseEngine INSTANCE = new MemoryDatabaseEngine();

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @Override
    public boolean useDatabase(UseSqlObject sqlObject, SqlContext context) {
        String databaseName = sqlObject.getDatabaseName();
        context.setCurrentDatabase(databaseName);
        return true;
    }

    @Override
    public boolean createDatabase(CreateDatabaseSqlObject sqlObject, SqlContext context) {
        String databaseName = sqlObject.getDatabaseName();

        if (sqlObject.isIfNotExists()) {
            DbObj oldDbObj = databases.putIfAbsent(databaseName, new DbObj());
            return oldDbObj == null;
        }
        if (sqlObject.isOrReplace()) {
            databases.put(databaseName, new DbObj());
            return true;
        }
        DbObj oldDbObj = databases.putIfAbsent(databaseName, new DbObj());
        if (oldDbObj != null) {
            if (sqlObject.isIfNotExists()) {
                // database already exists
                return false;
            } else {
                throw new EricaException(
                        "database already exists: " + databaseName);
            }
        }

        return true;
    }

    @Override
    public boolean dropDatabase(DropDatabaseSqlObject sqlObject, SqlContext context) {
        String databaseName = sqlObject.getDatabaseName();

        DbObj oldDbObj = databases.remove(databaseName);
        if (oldDbObj == null) {
            if (sqlObject.isIfExists()) {
                return false;
            }
            throw new EricaException(
                    "database doesn't exists: " + databaseName);
        }
        // clear context
        context.casCurrentDatabase(databaseName, null);
        return true;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    @Override
    public boolean createTable(CreateTableSqlObject sqlObject, SqlContext context) {
        return false;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

}
