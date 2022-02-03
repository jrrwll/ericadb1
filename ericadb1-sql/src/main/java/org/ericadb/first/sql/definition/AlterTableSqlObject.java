package org.ericadb.first.sql.definition;

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
public class AlterTableSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;
    List<ColumnDefinition> addColumnDefinitions;
    List<ColumnDefinition> modifyColumnDefinitions;
    List<String> dropColumns;

    public AlterTableSqlObject(String sql) {
        super(sql);
    }
}