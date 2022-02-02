package org.ericadb.first.sql.query;

import lombok.Getter;
import lombok.Setter;
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
    WhereSqlObject where;
    GroupBySqlObject groupBy;

    public SelectFromSqlObject(String sql) {
        super(sql);
    }
}
