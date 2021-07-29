package org.ericadb.first.store;

import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.sql.manipulation.DeleteFromSqlObject;
import org.ericadb.first.sql.manipulation.InsertIntoSqlObject;
import org.ericadb.first.sql.manipulation.UpdateSetSqlObject;
import org.ericadb.first.sql.query.SelectFromSqlObject;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
public interface TableEngine {

    int insertInto(InsertIntoSqlObject sqlObject, SqlContext context);

    int updateSet(UpdateSetSqlObject sqlObject, SqlContext context);

    int deleteFrom(DeleteFromSqlObject sqlObject, SqlContext context);

    ResultObjects selectFrom(SelectFromSqlObject sqlObject, SqlContext context);
}
