package org.ericadb.first.sql.definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ericadb.first.type.EType;

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
    TypeDefinition type;
    boolean notNull;
    Object defaultValue;
}
