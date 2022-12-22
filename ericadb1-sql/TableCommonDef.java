package org.dreamcat.common.sql;

import java.util.List;
import lombok.Data;

/**
 * @author Jerry Will
 * @version 2021-11-29
 */
@Data
public class TableCommonDef {

    private String name;
    private String comment;
    private List<ColumnCommonDef> columns;
    private boolean orReplace;
    private boolean ifNotExists;

    private IndexCommonDef primaryKey;
    // unique index/key `uk_c1_c2`(`c1`, `c2`)
    private List<IndexCommonDef> uniqueIndexes;
    private List<IndexCommonDef> indexes;
    // foreign key `fk_c1`(`c1`) references `t2`(`c2`)
    private List<ForeignKeyCommonDef> foreignKeys;

    private String rawProperties;
}
