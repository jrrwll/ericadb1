package org.ericadb.first.store.memory;

import org.ericadb.first.store.TableEngine;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
public class MemoryTableEngine implements TableEngine {


    public static TableEngine getInstance() {
        return INSTANCE;
    }

    static final TableEngine INSTANCE = new MemoryTableEngine();
}
