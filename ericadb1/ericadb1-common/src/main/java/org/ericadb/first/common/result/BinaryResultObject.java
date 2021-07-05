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
 * @since 2021-07-05
 */
@Getter
@AllArgsConstructor
public class BinaryResultObject implements ResultObject {

    byte[] value;

    @Override
    public EType getType() {
        return EType.BINARY;
    }

    @Override
    public void writeTo(OutputStream output) throws IOException {
        output.write(value);
    }

    public static ResultObject readFrom(InputStream input) throws IOException {
        byte[] sizeFlag = input.readNBytes(4);
        int size = ByteUtil.join(sizeFlag);
        byte[] value = new byte[size];
        input.read(value);
        return new BinaryResultObject(value);
    }
}
