package org.ericadb.first.sql.definition;

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
public class CreateTableSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;
    boolean ifNotExists;
    // sub item
    List<String> primaryColumnNames;
    List<ColumnDefinition> columnDefinitions = new ArrayList<>();
    List<IndexDefinition> indexDefinitions = new ArrayList<>();
    // props
    String engine;
    String defaultCharset;
    String comment;

    public CreateTableSqlObject(String sql) {
        super(sql);
    }
}
