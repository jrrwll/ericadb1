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
 * @since 2021/6/11
 */
@Getter
@AllArgsConstructor
public class TextResultObject implements ResultObject {

    String value;

    @Override
    public EType getType() {
        return EType.TEXT;
    }

    @Override
    public void writeTo(OutputStream output) throws IOException {
        byte[] body = value.getBytes();
        byte[] head = ByteUtil.split(body.length);
        output.write(head);
        output.write(body);
    }

    public static ResultObject readFrom(InputStream input) throws IOException {
        byte[] sizeFlag = new byte[4];
        input.read(sizeFlag);
        int size = ByteUtil.join(sizeFlag);
        byte[] value = new byte[size];
        input.read(value);
        String s = new String(value);
        return new TextResultObject(s);
    }
}
