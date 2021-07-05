package org.ericadb.first.context;

/**
 * @author Jerry Will
 * @since 2021-07-05
 */
public interface SqlContext {

    String getCurrentDatabase();

    void setCurrentDatabase(String newCurrentDatabase);

    boolean casCurrentDatabase(String expectCurrentDatabase, String newCurrentDatabase);

    static SqlContext createContext() {
        return new SimpleSqlContext();
    }
}
