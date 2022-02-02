package org.ericadb.first.sql.manipulation;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.sql.AbstractSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
@Getter
@Setter
public class InsertIntoSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;
    // null means all columns
    List<String> columnNames = new ArrayList<>();
    // values
    List<Object[]> values = new ArrayList<>();

    public InsertIntoSqlObject(String sql) {
        super(sql);
    }
}
