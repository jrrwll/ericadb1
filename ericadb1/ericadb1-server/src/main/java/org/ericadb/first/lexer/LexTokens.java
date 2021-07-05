package org.ericadb.first.lexer;

import java.util.Iterator;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
@Getter
@RequiredArgsConstructor
public class LexTokens implements Iterable<LexToken> {

    final List<LexToken> tokens;
    @Getter
    final String sql;

    @Override
    public Iterator<LexToken> iterator() {
        return tokens.iterator();
    }
}
