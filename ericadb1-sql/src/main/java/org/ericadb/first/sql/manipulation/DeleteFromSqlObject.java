package org.ericadb.first.sql.manipulation;

import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.sql.AbstractSqlObject;
import org.ericadb.first.sql.query.WhereObject;

/**
 * @author Jerry Will
 * @version 2021-07-22
 */
@Getter
@Setter
public class DeleteFromSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;
    WhereObject where;

    public DeleteFromSqlObject(String sql) {
        super(sql);
    }
}
