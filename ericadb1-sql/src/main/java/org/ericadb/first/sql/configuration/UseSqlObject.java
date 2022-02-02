package org.ericadb.first.sql.configuration;

import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.sql.AbstractSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-05
 */
@Getter
@Setter
public class UseSqlObject extends AbstractSqlObject {

    String databaseName;

    protected UseSqlObject(String sql) {
        super(sql);
    }
}
