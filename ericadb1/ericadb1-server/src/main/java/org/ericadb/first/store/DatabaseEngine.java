package org.ericadb.first.store;

import org.ericadb.first.context.SqlContext;
import org.ericadb.first.sql.configuration.UseSqlObject;
import org.ericadb.first.sql.definition.CreateDatabaseSqlObject;
import org.ericadb.first.sql.definition.CreateTableSqlObject;
import org.ericadb.first.sql.definition.DropDatabaseSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
public interface DatabaseEngine {

    boolean useDatabase(UseSqlObject sqlObject, SqlContext context);

    boolean createDatabase(CreateDatabaseSqlObject sqlObject, SqlContext context);

    boolean dropDatabase(DropDatabaseSqlObject sqlObject, SqlContext context);

    boolean createTable(CreateTableSqlObject sqlObject, SqlContext context);
}
