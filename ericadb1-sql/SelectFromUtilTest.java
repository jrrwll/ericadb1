package org.dreamcat.common.sql;

import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2022-08-03
 */
class SelectFromUtilTest {

    @Test
    void testParseOneValue() {
        // parseOneValueAndShow(
        //         "1", "3.14", "'abc'", "abc",
        //         "a is null", "3.14 is null", "'1' is not null",
        //         "null"
        // );
        // parseOneValueAndShow("1 + 1", "1 + 'a'", "'a' - 'b'");
        parseOneValueAndShow("1 * 1 / 1", "3.14 - 2.718 * 1");
    }

    @Test
    void testParseValue() {
        parseValueAndShow("a is not null and b between 'a' and 'z'");
    }

    private static void parseOneValueAndShow(String... valueSqls) {
        for (String valueSql : valueSqls) {
            ValueCommonDef value = SelectFromUtil.parseOneValue(
                    SqlUtil.lex(valueSql).listIterator());
            System.out.println(value);
        }
    }

    private static void parseValueAndShow(String valueSql) {
        ValueCommonDef value = SelectFromUtil.parseValue(
                SqlUtil.lex(valueSql).listIterator());
        System.out.println(value);
    }
}
