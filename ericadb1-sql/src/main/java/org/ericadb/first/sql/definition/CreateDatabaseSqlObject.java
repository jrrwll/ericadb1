package org.ericadb.first.sql.definition;

import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.sql.AbstractSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
@Getter
@Setter
public class CreateDatabaseSqlObject extends AbstractSqlObject {

    String databaseName;
    boolean ifNotExists;

    public CreateDatabaseSqlObject(String sql) {
        super(sql);
    }
}
