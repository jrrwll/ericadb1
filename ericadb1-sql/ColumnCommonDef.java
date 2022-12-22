package org.dreamcat.common.sql;

import java.util.List;
import lombok.Data;

/**
 * @author Jerry Will
 * @version 2021-12-06
 */
@Data
public class ColumnCommonDef {

    private String name;
    private String type;
    private boolean unsigned;
    private List<Integer> typeParams; // decimal(16, 6), varchar(255)
    private boolean notNull;
    private String defaultValue;
    private String comment;

    private String onUpdate;
    private boolean autoIncrement;
}
