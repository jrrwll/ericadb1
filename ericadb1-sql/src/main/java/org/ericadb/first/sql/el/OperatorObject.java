package org.ericadb.first.sql.el;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dreamcat.round.lex.OperatorToken;

/**
 * @author Jerry Will
 * @version 2022-02-06
 */
@Getter
@RequiredArgsConstructor
public class OperatorObject extends ElObject {

    private final OperatorToken operator;
}
