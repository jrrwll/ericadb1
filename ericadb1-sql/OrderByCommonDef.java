package org.dreamcat.common.sql;

import lombok.Data;

/**
 * @author Jerry Will
 * @version 2022-08-02
 */
@Data
public class OrderByCommonDef {

    private ValueCommonDef value;
    private boolean desc;
}
