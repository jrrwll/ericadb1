package org.ericadb.first.sql.query;

import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.sql.AbstractSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
@Getter
@Setter
public class SelectSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;

    WhereSqlObject where;
    GroupBySqlObject groupBy;
    WhereSqlObject having;
    int limitSize;
    int limitOffset;

    public SelectSqlObject(String sql) {
        super(sql);
    }
}
