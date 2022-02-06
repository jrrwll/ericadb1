package org.ericadb.first.sql.query;

import lombok.Getter;
import lombok.Setter;
import org.ericadb.first.sql.el.ElObject;

/**
 * @author Jerry Will
 * @version 2021-02-03
 */
@Getter
@Setter
public class JoinObject {

    JoinType type;
    ElObject on;
}
