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
public class DropDatabaseSqlObject extends AbstractSqlObject {

    String databaseName;
    boolean ifExists;

    public DropDatabaseSqlObject(String sql, String databaseName, boolean ifExists) {
        super(sql);
        this.databaseName = databaseName;
        this.ifExists = ifExists;
    }
}
