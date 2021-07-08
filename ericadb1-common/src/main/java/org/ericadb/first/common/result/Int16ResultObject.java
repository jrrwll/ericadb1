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
public class Int16ResultObject implements ResultObject {

    short value;

    @Override
    public EType getType() {
        return EType.INT16;
    }

    @Override
    public void writeTo(OutputStream output) throws IOException {
        output.write((value & 0xff00) >> 8);
        output.write(value & 0xff);
    }

    public static ResultObject readFrom(InputStream input) throws IOException {
        int a = input.read();
        int b = input.read();
        return new Int16ResultObject((short) ((a << 8) + b));
    }
}
