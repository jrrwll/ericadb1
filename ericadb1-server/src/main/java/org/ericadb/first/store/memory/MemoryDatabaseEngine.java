package org.ericadb.first.store.memory;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.exception.EricaException;
import org.ericadb.first.sql.configuration.UseSqlObject;
import org.ericadb.first.sql.definition.AlterTableSqlObject;
import org.ericadb.first.sql.definition.CreateDatabaseSqlObject;
import org.ericadb.first.sql.definition.CreateTableSqlObject;
import org.ericadb.first.sql.definition.DescTableSqlObject;
import org.ericadb.first.sql.definition.DropDatabaseSqlObject;
import org.ericadb.first.sql.definition.DropTableSqlObject;
import org.ericadb.first.store.DatabaseEngine;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
@Slf4j
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

        DbObj dbObj = new DbObj(databaseName);
        if (sqlObject.isOrReplace()) {
            databases.put(databaseName, dbObj);
            return true;
        }

        DbObj oldDbObj = databases.putIfAbsent(databaseName, dbObj);
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
            throw new EricaException("database doesn't exists: " + databaseName);
        }
        // clear context
        if (!context.casCurrentDatabase(databaseName, null)) {
            log.warn("failed to CAS current database: {}", databaseName);
        }
        return true;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    @Override
    public boolean createTable(CreateTableSqlObject sqlObject, SqlContext context) {
        String databaseName = sqlObject.getDatabaseName();
        DbObj dbObj = getDatabase(databaseName, context);

        String tableName = sqlObject.getTableName();
        TableObj tableObj = ofTableObj(sqlObject);

        if (sqlObject.isOrReplace()) {
            dbObj.tables.put(tableName, tableObj);
            return true;
        }

        TableObj oldTableObj = dbObj.tables.putIfAbsent(tableName, tableObj);
        if (oldTableObj != null) {
            if (sqlObject.isIfNotExists()) {
                // table already exists
                return false;
            } else {
                throw new EricaException("table already exists: " + databaseName + "." + tableName);
            }
        }

        return true;
    }

    @Override
    public boolean dropTable(DropTableSqlObject sqlObject, SqlContext context) {
        String databaseName = sqlObject.getDatabaseName();
        DbObj dbObj = getDatabase(databaseName, context);

        String tableName = sqlObject.getTableName();
        TableObj tableObj = dbObj.tables.remove(tableName);
        if (tableObj == null) {
            if (sqlObject.isIfExists()) {
                return false;
            }
            throw new EricaException("table doesn't exists: " + databaseName + "." + tableName);
        }
        return true;
    }

    @Override
    public boolean alterTable(AlterTableSqlObject sqlObject, SqlContext context) {
        throw new EricaException("Alter table is unsupported");
    }

    @Override
    public ResultObjects descTable(DescTableSqlObject sqlObject, SqlContext context) {
        String databaseName = sqlObject.getDatabaseName();
        DbObj dbObj = getDatabase(databaseName, context);

        String tableName = sqlObject.getTableName();
        TableObj tableObj = dbObj.tables.get(tableName);
        if (tableObj == null) {
            throw new EricaException("table doesn't exists: " + databaseName + "." + tableName);
        }

        try {
            return toResultObjects(tableObj);
        } catch (IOException e) {
            throw new EricaException(e);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

}
