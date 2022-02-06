package org.ericadb.first.sql.query;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jerry Will
 * @version 2022-01-31
 */
@Getter
@Setter
public class GroupByObject {

    List<ElObject> columns;
    WhereObject having;
}
