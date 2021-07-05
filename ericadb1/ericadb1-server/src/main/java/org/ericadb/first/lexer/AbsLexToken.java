package org.ericadb.first.lexer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import org.dreamcat.common.util.ObjectUtil;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
@Getter
public class AbsLexToken implements LexToken {

    Type type;
    String rawToken;

    protected AbsLexToken(Type type, String rawToken) {
        this.type = type;
        this.rawToken = rawToken;
    }

    @Override
    public String toString() {
        return this.getRawToken();
    }

    public static String getRawToken(List<LexToken> tokens) {
        if (ObjectUtil.isEmpty(tokens)) return "";

        return tokens.stream().map(Objects::toString)
                .collect(Collectors.joining(" "));
    }
}
