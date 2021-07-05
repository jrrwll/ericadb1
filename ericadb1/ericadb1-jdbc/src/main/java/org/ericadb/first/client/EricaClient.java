package org.ericadb.first.client;

import java.io.Closeable;
import java.sql.ResultSet;


/**
 * @author Jerry Will
 * @since 2021/6/10
 */
public interface EricaClient extends Closeable {

    ResultSet execute(String sql);

    boolean isClosed();
}