package org.ericadb.first.common.result;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ByteUtil;
import org.ericadb.first.common.type.EType;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class ResultObjects {

    final String[] head;
    final ResultObject[][] body;

    public static ResultObjects singleton(ResultObject object) {
        ResultObject[][] body = {{object}};
        return new ResultObjects(SINGLETON_HEAD, body);
    }

    public void writeTo(OutputStream output) throws IOException {
        ResultObject[] row = body[0];
        // shape
        writeShape(output, row);

        // type flags
        writeTypes(output, row);

        // head data
        writeHead(output);

        // body data
        for (ResultObject[] i : body) {
            for (ResultObject object : i) {
                object.writeTo(output);
            }
        }
    }

    private void writeShape(OutputStream output, ResultObject[] row) throws IOException {
        int rowSize = body.length;
        int columnSize = row.length;
        output.write(ByteUtil.split(rowSize));
        output.write(ByteUtil.split(columnSize));
    }

    private void writeTypes(OutputStream output, ResultObject[] row) throws IOException {
        for (ResultObject cell : row) {
            EType type = cell.getType();
            output.write(type.ordinal());
        }
    }

    private void writeHead(OutputStream output) throws IOException {
        String s = String.join("\0", head);
        int headSize = s.length();
        output.write(ByteUtil.split(headSize));
        output.write(s.getBytes());
    }

    public static ResultObjects readFrom(InputStream input) throws IOException {
        // shape
        int rowSize = readSize(input);
        int columnSize = readSize(input);

        // type flags
        EType[] types = readTypes(input, columnSize);

        // head data
        String[] head = readHead(input);

        // body data
        ResultObject[][] body = new ResultObject[rowSize][];
        for (int i = 0; i < rowSize; i++) {
            ResultObject[] row = new ResultObject[columnSize];
            body[i] = row;
            for (int j = 0; j < columnSize; j++) {
                row[j] = types[j].readFrom(input);
            }
        }

        // log.debug("receive from server: {}", s);
        return new ResultObjects(head, body);
    }

    private static int readSize(InputStream input) throws IOException {
        byte[] sizeFlag = new byte[4];
        input.read(sizeFlag);
        return ByteUtil.join(sizeFlag);
    }

    private static EType[] readTypes(InputStream input, int columnSize) throws IOException {
        byte[] typeFlags = new byte[columnSize];
        input.read(typeFlags);
        EType[] types = new EType[columnSize];
        for (int i = 0; i < columnSize; i++) {
            types[i] = EType.values()[typeFlags[i]];
        }
        return types;
    }

    private static String[] readHead(InputStream input) throws IOException {
        byte[] headFlag = new byte[4];
        input.read(headFlag);
        int headSize = ByteUtil.join(headFlag);
        if (headSize == 0) return SINGLETON_HEAD;
        byte[] headBytes = new byte[headSize];
        input.read(headBytes);
        return new String(headBytes).split("\0");
    }

    private static final String[] SINGLETON_HEAD = new String[]{""};
}
