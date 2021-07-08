package org.ericadb.first.server;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.ericadb.first.server.socket.EricaSocketServer;

/**
 * @author Jerry Will
 * @since 2021/6/11
 */
@Slf4j
public class EricaLauncher {

    public static void main(String[] args) throws IOException {
        int port = 1314;
        if (args.length >= 1) port = Integer.parseInt(args[0]);

        EricaSocketServer server = new EricaSocketServer(port);
        log.info("EricaDB1 started on port {}", port);
        server.runForLoop();
    }

}
