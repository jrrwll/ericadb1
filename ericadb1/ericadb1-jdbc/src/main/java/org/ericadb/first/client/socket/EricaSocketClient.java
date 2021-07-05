package org.ericadb.first.client.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ByteUtil;
import org.ericadb.first.client.EricaClient;
import org.ericadb.first.client.EricaClientProps;
import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.jdbc.EricaResultSet;


/**
 * @author Jerry Will
 * @since 2021/6/10
 */
@Slf4j
public class EricaSocketClient implements EricaClient {

    Socket socket;

    public EricaSocketClient(EricaClientProps props) throws IOException {
        String host = props.getHost();
        int port = props.getPort();
        this.socket = new Socket(host, port);
    }

    @Override
    public ResultSet execute(String sql) {
        ensureOpen();
        try (OutputStream output = socket.getOutputStream()) {
            if (log.isDebugEnabled()) {
                log.info("send sql to server: {}", sql);
            }
            var clientBody = sql.getBytes(StandardCharsets.UTF_8);
            var clientHead = ByteUtil.split(clientBody.length);
            output.write(clientHead);
            output.write(clientBody);

            try (InputStream input = socket.getInputStream()) {
                ResultObjects resultObjects = ResultObjects.readFrom(input);
                return new EricaResultSet(resultObjects);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isClosed() {
        return socket.isClosed();
    }

    private void ensureOpen() {
        if (isClosed()) {
            throw new RuntimeException("socket is closed");
        }
    }

    @Override
    public void close() throws IOException {
        if (!isClosed()) {
            socket.close();
        }
    }

}
