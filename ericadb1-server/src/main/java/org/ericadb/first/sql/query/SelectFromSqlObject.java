package org.ericadb.first.sql.query;

import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.sql.AbstractSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
@Getter
@Setter
public class SelectFromSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;
    int limitSize;
    int limitOffset;

    public SelectFromSqlObject(String sql) {
        super(sql);
    }

    @Override
    public ResultObjects execute(SqlContext context) {
        throw new UnsupportedOperationException();
    }
}
