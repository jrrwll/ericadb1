package org.ericadb.first.context;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
@Getter
@Setter
@SuppressWarnings("unchecked")
@AllArgsConstructor
public class Tuple {

    public Object[] values;

    public static Tuple of(Object... values) {
        return new Tuple(values);
    }

    public <T> T at(int index) {
        return (T) values[index];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return Arrays.equals(values, tuple.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
}
