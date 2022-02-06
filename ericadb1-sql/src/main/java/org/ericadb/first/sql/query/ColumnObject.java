package org.ericadb.first.sql.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jerry Will
 * @version 2021-02-03
 */
@Getter
@Setter
public class ColumnObject {

    ElObject column; // selected columns
    String as; // alias
}
