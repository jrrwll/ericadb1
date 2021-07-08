package org.ericadb.first.processor;

import java.sql.ResultSet;
import org.ericadb.first.common.result.ResultObjects;
import org.ericadb.first.jdbc.EricaResultSet;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
public final class ResultObjectProcessors {

    private ResultObjectProcessors() {
    }

    public static ResultSet process(ResultObjects ros) {
        return new EricaResultSet(ros.getHead(), ros.getBody());
    }

}
