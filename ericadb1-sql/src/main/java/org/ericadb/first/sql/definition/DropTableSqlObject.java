package org.ericadb.first.sql.definition;

import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.sql.AbstractSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
@Getter
@Setter
public class DropTableSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;
    boolean ifExists;

    public DropTableSqlObject(String sql) {
        super(sql);
    }
}