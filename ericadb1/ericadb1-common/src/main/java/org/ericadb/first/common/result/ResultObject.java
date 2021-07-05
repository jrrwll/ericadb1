package org.ericadb.first.common.result;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.dreamcat.common.function.ExpFunction;
import org.ericadb.first.common.type.EType;

/**
 * @author Jerry Will
 * @since 2021/6/11
 */
public interface ResultObject {

    EType getType();

    void writeTo(OutputStream output) throws IOException;

    default boolean isNull() {
        return false;
    }

    ResultObject NULL = new ResultObject() {

        @Override
        public EType getType() {
            return EType.NULL;
        }

        @Override
        public void writeTo(OutputStream output) throws IOException {
            // nop
        }

        @Override
        public boolean isNull() {
            return true;
        }
    };

    ExpFunction<InputStream, ResultObject, IOException> NULL_READER = it -> NULL;
}
