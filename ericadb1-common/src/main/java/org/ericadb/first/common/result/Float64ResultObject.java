package org.ericadb.first.common.result;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dreamcat.common.util.ByteUtil;
import org.ericadb.first.common.type.EType;

/**
 * @author Jerry Will
 * @since 2021-07-05
 */
@Getter
@AllArgsConstructor
public class Float64ResultObject implements ResultObject {

    double value;

    @Override
    public EType getType() {
        return EType.FLOAT64;
    }

    @Override
    public void writeTo(OutputStream output) throws IOException {
        output.write(ByteUtil.split((long) value));
    }

    /**
     * read a float64 from input stream
     *
     * @param input input stream
     * @return a float64
     * @throws IOException I/O error
     * @see Double#longBitsToDouble(long)
     */
    public static ResultObject readFrom(InputStream input) throws IOException {
        byte[] bytes = input.readNBytes(8);
        double value = ByteBuffer.wrap(bytes).getDouble();
        return new Float64ResultObject(value);
    }
}
