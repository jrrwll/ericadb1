package org.ericadb.first.sql.el;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Jerry Will
 * @version 2022-02-06
 */
@Getter
@RequiredArgsConstructor
public class ConstObject extends ElObject {

    public final Object value;
}
