package org.ericadb.first.sql.definition;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jerry Will
 * @version 2022-01-30
 */
@Getter
@Setter
public class IndexDefinition {

    String name;
    boolean unique;
    List<String> columnNames;
}
