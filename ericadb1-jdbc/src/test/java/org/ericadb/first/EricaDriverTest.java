package org.ericadb.first;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.dreamcat.common.Pair;
import org.dreamcat.common.io.FileUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
@SuppressWarnings("unchecked")
class EricaDriverTest {

    static final String create_database_url = "jdbc:ericadb://127.0.0.1:1314";
    static final String will_url = "jdbc:ericadb://127.0.0.1:1314/will";

    static String createDatabaseSql;
    static List<String> willSql;

    @BeforeAll
    static void init() throws Exception {
        Class.forName("org.ericadb.first.EricaDriver");

        String str = FileUtil.readAsString(EricaDriver.class.getClassLoader()
                .getResource("erica.sql").getFile());
        String[] a = str.split(";");
        willSql = new ArrayList<>();
        for (String s : a) {
            s = s.trim();
            if (s.isBlank()) continue;
            String sql = s + ";";
            // System.out.printf("<%s>\n", sql);

            if (sql.contains("database")) {
                createDatabaseSql = sql;
            } else if (!sql.startsWith("use")) {
                willSql.add(sql);
            }
        }
    }

    @Test
    void test_exe_sql_create_database() throws SQLException {
        execute(create_database_url, Pair.of(createDatabaseSql, true));
    }

    @Test
    void test_exe_sql() throws Exception {
        execute(will_url, willSql.stream()
                .map(sql -> Pair.of(sql, sql.startsWith("select")))
                .toArray(Pair[]::new));
    }

    @SafeVarargs
    final void execute(String url, Pair<String, Boolean>... pairs) throws SQLException {
        Driver driver = DriverManager.getDriver(url);

        try (Connection connection = driver.connect(url, new Properties())) {
            try (Statement statement = connection.createStatement()) {
                for (Pair<String, Boolean> pair : pairs) {
                    String sql = pair.first();
                    if (pair.second()) {
                        int rowAffect = statement.executeUpdate(sql);
                        System.out.printf("executeUpdate(rowAffect=%d):\t%s\n", rowAffect, sql);
                    } else {
                        try (ResultSet rs = statement.executeQuery(sql)) {
                            System.out.printf("executeQuery(fetchSize=%d):\t%s\n",
                                    rs.getFetchSize(), sql);
                            while (rs.next()) {
                                System.out.printf("next row:\t%s, %s\n",
                                        rs.getObject(1),
                                        rs.getObject(2));
                            }
                        }

                    }
                }
            }
        }
    }
}
