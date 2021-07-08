package org.ericadb.first;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author Jerry Will
 * @since 2021-07-05
 */
public class EricaDriverInteractiveTest {

    static final String url = "jdbc:ericadb://127.0.0.1:1314";
    static final String url_use_will = "jdbc:ericadb://127.0.0.1:1314/will";

    public static void main(String[] args) throws Exception {
        Class.forName("org.ericadb.first.EricaDriver");

        Driver driver = DriverManager.getDriver(url);
        try (Connection connection = driver.connect(url, new Properties())) {
            try (Statement statement = connection.createStatement()) {
                for (; ; ) {
                    String sql = readSql();
                    System.out.println("SQL:\t" + sql);
                    if (sql.equals("exit")) break;

                    if (!isQuery(sql)) {
                        int rowAffect = statement.executeUpdate(sql);
                        System.out.printf("executeUpdate(rowAffect=%d):\t%s\n", rowAffect, sql);
                        continue;
                    }

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

    private static boolean isQuery(String sql) {
        sql = sql.toLowerCase();
        int size = sql.length();
        for (int i = 0; i < size; i++) {
            char c = sql.charAt(i);
            if (c <= ' ') continue;

            return c == 's' && i < size - 6
                    && sql.substring(i, i + 6).equals("select");
        }
        return false;
    }

    public static String readSql() throws IOException {
        StringBuilder sql = new StringBuilder();
        String line;
        System.out.print("type your SQL here>> ");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (!(line = br.readLine()).isBlank()) {
                if (line.endsWith(";")) {
                    sql.append(line.substring(0, line.length() - 1));
                    break;
                }
                sql.append(line).append('\n');
            }
        }
        System.out.println();
        return sql.toString();
    }
}
