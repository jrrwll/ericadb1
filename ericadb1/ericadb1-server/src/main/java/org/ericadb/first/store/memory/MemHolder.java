package org.ericadb.first.store.memory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.ericadb.first.common.type.EType;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
class MemHolder {

    Map<String, DbObj> databases = new ConcurrentHashMap<>();

    static class DbObj {

        Map<String, TableObj> tables = new ConcurrentHashMap<>();
    }

    static class TableObj {

        Map<String, ColumnObj> meta = new ConcurrentHashMap<>();
        // id  -->  one row data
        Map<String, List<Object>> rows = new ConcurrentHashMap<>();
    }

    static class ColumnObj {

        int index;
        String name;
        EType type;
        boolean nullable;
        Object defaultValue;
    }
}
