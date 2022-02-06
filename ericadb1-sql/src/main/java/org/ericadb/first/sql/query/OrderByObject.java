package org.ericadb.first.sql.query;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jerry Will
 * @version 2021-02-03
 */
@Getter
@Setter
public class OrderByObject {

    List<ElObject> columns;
    boolean desc;
}
