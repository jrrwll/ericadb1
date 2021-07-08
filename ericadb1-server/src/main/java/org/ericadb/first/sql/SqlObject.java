package org.ericadb.first.sql;

import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.context.SqlContext;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
public interface SqlObject {

    @Override
    String toString();

    String getSql();

    ResultObjects execute(SqlContext context);
}
