package org.ericadb.first.sql.definition;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.sql.AbstractSqlObject;

/**
 * @author Jerry Will
 * @version 2022-02-01
 */
@Getter
@Setter
public class CreateIndexSqlObject extends AbstractSqlObject  {

    String databaseName;
    String tableName;
    String indexName;
    boolean unique;
    List<String> columnNames;

    public CreateIndexSqlObject(String sql) {
        super(sql);
    }
}
