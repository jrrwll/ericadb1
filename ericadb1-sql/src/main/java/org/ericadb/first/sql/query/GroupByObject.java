package org.ericadb.first.sql.query;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.sql.el.ElObject;

/**
 * @author Jerry Will
 * @version 2022-01-31
 */
@Getter
@Setter
public class GroupByObject {

    List<ElObject> columns;
    ElObject having;
}
