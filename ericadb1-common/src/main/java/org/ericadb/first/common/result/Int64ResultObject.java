package org.ericadb.first.common.result;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dreamcat.common.util.ByteUtil;
import org.ericadb.first.common.type.EType;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
@Getter
@AllArgsConstructor
public class Int64ResultObject implements ResultObject {

    long value;

    @Override
    public EType getType() {
        return EType.INT64;
    }

    @Override
    public void writeTo(OutputStream output) throws IOException {
        output.write(ByteUtil.split(value));
    }

    public static ResultObject readFrom(InputStream input) throws IOException {
        byte[] bytes = input.readNBytes(4);
        return new Int64ResultObject(ByteUtil.longJoin(bytes));
    }
}
