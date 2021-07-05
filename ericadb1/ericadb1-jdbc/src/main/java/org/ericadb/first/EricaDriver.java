package org.ericadb.first;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;
import org.ericadb.first.jdbc.EricaConnection;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
public class EricaDriver implements Driver {

    public static final EricaDriver INSTANCE = new EricaDriver();

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        try {
            return new EricaConnection(url, info);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        // jdbc:ericadb://127.0.0.1:3306/jerry
        return url.startsWith("jdbc:ericadb://");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        // return empty array since no properties are required
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 1;
    }

    @Override
    public boolean jdbcCompliant() {
        // no full support for SQL 92 Entry Level
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("the erica-first driver does not use java.util.logging");
    }

    static {
        try {
            DriverManager.registerDriver(INSTANCE);
        } catch (SQLException ignore) {
        }
    }
}
