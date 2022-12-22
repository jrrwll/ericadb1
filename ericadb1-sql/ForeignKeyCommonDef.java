package org.dreamcat.common.sql;

import lombok.Data;

/**
 * @author Jerry Will
 * @version 2021-11-29
 */
@Data
public class ForeignKeyCommonDef {

    private String name;
    private String column;
    private String referenceTable;
    private String referenceColumn;
}
