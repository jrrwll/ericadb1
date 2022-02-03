package org.ericadb.first.sql.manipulation;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.dreamcat.common.Pair;
import org.ericadb.first.sql.AbstractSqlObject;
import org.ericadb.first.sql.query.WhereSqlObject;

/**
 * @author Jerry Will
 * @version 2021-07-22
 */
@Getter
@Setter
public class UpdateSetSqlObject extends AbstractSqlObject {

    String databaseName;
    String tableName;
    List<Pair<String, Object>> sets = new ArrayList<>();
    WhereSqlObject where;

    public UpdateSetSqlObject(String sql) {
        super(sql);
    }
}
