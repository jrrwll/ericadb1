package org.ericadb.first.store;

import org.ericadb.first.context.SqlContext;
import org.ericadb.first.sql.manipulation.InsertIntoSqlObject;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
public interface TableEngine {

    int insertInto(InsertIntoSqlObject sqlObject, SqlContext context);
}
