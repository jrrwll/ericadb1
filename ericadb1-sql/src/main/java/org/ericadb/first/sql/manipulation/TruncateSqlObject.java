package org.ericadb.first.sql.manipulation;

import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.sql.AbstractSqlObject;

/**
 * @author Jerry Will
 * @version 2022-02-01
 */
@Getter
@Setter
public class TruncateSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;

    public TruncateSqlObject(String sql) {
        super(sql);
    }
}
