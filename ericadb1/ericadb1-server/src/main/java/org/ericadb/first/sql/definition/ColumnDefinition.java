package org.ericadb.first.sql.definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ericadb.first.common.result.ResultObject;
import org.ericadb.first.common.type.EType;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDefinition {

    String columnName;
    EType type;
    boolean notNull;
    ResultObject defaultValue;

    public ColumnDefinition(String columnName, EType type) {
        this.columnName = columnName;
        this.type = type;
    }
}
