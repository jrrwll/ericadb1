package org.ericadb.first.common.result;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ericadb.first.common.type.EType;

/**
 * @author Jerry Will
 * @since 2021-07-05
 */
@Getter
@AllArgsConstructor
public class BoolResultObject implements ResultObject {

    boolean value;

    @Override
    public EType getType() {
        return EType.BOOL;
    }

    @Override
    public void writeTo(OutputStream output) throws IOException {
        output.write(value ? 1 : 0);
    }

    public static final ResultObjects TRUE = ResultObjects.singleton(
            new BoolResultObject(true));
    public static final ResultObjects FALSE = ResultObjects.singleton(
            new BoolResultObject(false));

    public static ResultObject readFrom(InputStream input) throws IOException {
        int value = input.read();
        return new BoolResultObject(value == 1);
    }
}
