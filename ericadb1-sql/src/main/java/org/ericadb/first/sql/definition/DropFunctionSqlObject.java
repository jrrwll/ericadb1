package org.ericadb.first.sql.definition;

import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.sql.AbstractSqlObject;

/**
 * @author Jerry Will
 * @version 2022-02-01
 */
@Getter
@Setter
public class DropFunctionSqlObject extends AbstractSqlObject {

    public DropFunctionSqlObject(String sql) {
        super(sql);
    }
}
