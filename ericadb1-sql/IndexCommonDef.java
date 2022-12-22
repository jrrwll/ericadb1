package org.dreamcat.common.sql;

import java.util.List;
import lombok.Data;

/**
 * @author Jerry Will
 * @version 2021-12-06
 */
@Data
public class IndexCommonDef {

    private String name;
    private List<String> columns;
}
