package org.ericadb.first.common.result;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ericadb.first.common.type.EType;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
@Getter
@AllArgsConstructor
public class Int8ResultObject implements ResultObject {

    byte value;

    @Override
    public EType getType() {
        return EType.INT8;
    }

    @Override
    public void writeTo(OutputStream output) throws IOException {
        output.write(value);
    }

    public static ResultObject readFrom(InputStream input) throws IOException {
        int value = input.read();
        return new Int8ResultObject((byte) value);
    }
}
