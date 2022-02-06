package org.ericadb.first.sql.query;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.sql.AbstractSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
@Getter
@Setter
public class SelectSqlObject extends AbstractSqlObject {

    List<ColumnObject> columns; // selected columns;
    List<FromObject> from;
    List<JoinObject> join; // from.size() - join.size() == 1

    WhereObject where;
    GroupByObject groupBy;
    OrderByObject orderBy;
    LimitObject limit;

    List<SelectSqlObject> union;

    public SelectSqlObject(String sql) {
        super(sql);
    }
}
