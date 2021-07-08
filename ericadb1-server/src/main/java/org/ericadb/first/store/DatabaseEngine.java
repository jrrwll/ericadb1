package org.ericadb.first.store;

import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.sql.configuration.UseSqlObject;
import org.ericadb.first.sql.definition.AlterTableSqlObject;
import org.ericadb.first.sql.definition.CreateDatabaseSqlObject;
import org.ericadb.first.sql.definition.CreateTableSqlObject;
import org.ericadb.first.sql.definition.DescTableSqlObject;
import org.ericadb.first.sql.definition.DropDatabaseSqlObject;
import org.ericadb.first.sql.definition.DropTableSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
public interface DatabaseEngine {

    boolean useDatabase(UseSqlObject sqlObject, SqlContext context);

    boolean createDatabase(CreateDatabaseSqlObject sqlObject, SqlContext context);

    boolean dropDatabase(DropDatabaseSqlObject sqlObject, SqlContext context);

    boolean createTable(CreateTableSqlObject sqlObject, SqlContext context);

    boolean dropTable(DropTableSqlObject sqlObject, SqlContext context);

    boolean alterTable(AlterTableSqlObject sqlObject, SqlContext context);

    ResultObjects descTable(DescTableSqlObject sqlObject, SqlContext context);
}
