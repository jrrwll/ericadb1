package org.ericadb.first.server.socket;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ByteUtil;
import org.ericadb.first.context.SqlContext;
import org.ericadb.first.server.EricaServer;

/**
 * @author Jerry Will
 * @since 2021/6/11
 */
@Slf4j
public class EricaSocketServer implements EricaServer, Closeable {

    int port;
    ServerSocket server;
    volatile boolean loop = true;

    public EricaSocketServer(int port) throws IOException {
        this.port = port;
        this.server = new ServerSocket(port);
    }

    public void runForLoop() throws IOException {
        while (loop) {
            Socket socket = server.accept();
            InetAddress address = socket.getInetAddress();
            SqlContext context = SqlContext.createContext();
            try (var input = socket.getInputStream()) {
                while (socket.isConnected()) {
                    byte[] sizeFlag = input.readNBytes(4);
                    int size = ByteUtil.join(sizeFlag);
                    byte[] data = new byte[size];
                    input.read(data);
                    String sql = new String(data);
                    log.info("receive from client({}): {}", address, sql);

                    var resultObjects = this.execute(sql, context);
                    try (OutputStream output = socket.getOutputStream()) {
                        resultObjects.writeTo(output);
                    }
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        loop = false;
        if (!server.isClosed()) {
            server.close();
        }
    }
}