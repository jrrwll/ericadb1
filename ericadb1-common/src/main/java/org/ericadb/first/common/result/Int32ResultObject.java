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
public class Int32ResultObject implements ResultObject {

    int value;

    @Override
    public EType getType() {
        return EType.INT32;
    }

    @Override
    public void writeTo(OutputStream output) throws IOException {
        output.write(ByteUtil.split(value));
    }

    public static ResultObject readFrom(InputStream input) throws IOException {
        byte[] bytes = input.readNBytes(4);
        return new Int32ResultObject(ByteUtil.join(bytes));
    }

    public static final ResultObjects ONE = ResultObjects.singleton(
            new Int32ResultObject(1));
    public static final ResultObjects ZERO = ResultObjects.singleton(
            new Int32ResultObject(0));
}
