package org.ericadb.first.store.memory;

import org.ericadb.first.store.DatabaseEngine;
import org.ericadb.first.store.StorageEngineManager;
import org.ericadb.first.store.TableEngine;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
public class MemoryStorageEngineManager implements StorageEngineManager {

    @Override
    public DatabaseEngine getDatabaseEngine() {
        return MemoryDatabaseEngine.getInstance();
    }

    @Override
    public TableEngine getTableEngine() {
        return MemoryTableEngine.getInstance();
    }
}
