package org.ericadb.first.context;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Jerry Will
 * @since 2021-07-05
 */
public class SimpleSqlContext implements SqlContext {

    AtomicReference<String> currentDatabase;

    @Override
    public String getCurrentDatabase() {
        return this.currentDatabase.get();
    }

    @Override
    public void setCurrentDatabase(String newCurrentDatabase) {
        this.currentDatabase.set(newCurrentDatabase);
    }

    @Override
    public boolean casCurrentDatabase(String expectCurrentDatabase, String newCurrentDatabase) {
        return this.currentDatabase.compareAndSet(expectCurrentDatabase, newCurrentDatabase);
    }
}
