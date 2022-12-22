package org.dreamcat.common.sql;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @author Jerry Will
 * @version 2022-08-02
 */
@Data
public class SelectFromCommonDef {

    private List<SelectFromCommonDef> union; // only union

    private boolean distinct;
    private List<ValueCommonDef> select = new ArrayList<>();
    private List<JoinCommonDef> from;
    private SelectFromCommonDef subQueryFrom; // sub-query
    private String subQueryAlias;

    private ValueCommonDef where;
    private List<ValueCommonDef> groupBy;
    private ValueCommonDef having;
    private List<OrderByCommonDef> orderBy;
    private Long limit;
    private Long offset;

    public static SelectFromCommonDef fromUnion(List<SelectFromCommonDef> union) {
        SelectFromCommonDef selectFrom = new SelectFromCommonDef();
        selectFrom.setUnion(union);
        return selectFrom;
    }
}
