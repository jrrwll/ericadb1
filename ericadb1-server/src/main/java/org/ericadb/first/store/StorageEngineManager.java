package org.ericadb.first.store;

import org.ericadb.first.store.memory.MemoryStorageEngineManager;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
public interface StorageEngineManager {

    DatabaseEngine getDatabaseEngine();

    TableEngine getTableEngine();

    static StorageEngineManager getInstance() {
        return INSTANCE;
    }

    StorageEngineManager INSTANCE = new MemoryStorageEngineManager();
}
