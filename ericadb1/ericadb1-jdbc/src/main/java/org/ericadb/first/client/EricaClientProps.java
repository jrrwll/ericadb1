package org.ericadb.first.client;

import java.util.Properties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jerry Will
 * @since 2021/6/10
 */
@Data
@NoArgsConstructor
public class EricaClientProps {

    String host = "localhost";
    int port = 1314;

    String user;
    String password;

    public EricaClientProps(Properties props) {
        this.user = props.getProperty("user");
        this.password = props.getProperty("password");
    }

    // jdbc:ericadb://127.0.0.1:3306/jerry
    public static EricaClientProps fromUrl(String url, Properties info) {
        EricaClientProps props = new EricaClientProps(info);

        // 15 for `jdbc:ericadb://`
        int portStart = url.indexOf(':', 15);
        int databaseStart = url.indexOf('/', 15);
        if (databaseStart == -1) databaseStart = url.length();
        // has port in url
        if (portStart != -1) {
            props.host = url.substring(15, portStart);
            props.port = Integer.parseInt(url.substring(portStart + 1, databaseStart));
        } else {
            props.host = url.substring(15, databaseStart);
        }

        return props;
    }
}
