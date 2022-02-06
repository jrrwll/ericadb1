package org.ericadb.first.sql.definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDefinition {

    int index;
    String columnName;
    TypeObject type;
    boolean notNull;
    Object defaultValue;
    boolean autoIncrement;
    String onUpdate;
    String comment;
}
