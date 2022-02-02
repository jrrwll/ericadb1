package org.ericadb.first.syntax;

import static org.ericadb.first.lex.KeywordToken.INDEX;
import static org.ericadb.first.lex.KeywordToken.KEY;
import static org.ericadb.first.lex.KeywordToken.PRIMARY;
import static org.ericadb.first.lex.KeywordToken.UNIQUE;

import org.dreamcat.round.lex.IdentifierToken;
import org.dreamcat.round.lex.RoundToken;
import org.dreamcat.round.lex.StringToken;
import org.dreamcat.round.lex.TokenInfoStream;

/**
 * @author Jerry Will
 * @version 2022-01-31
 */
class Companion {

    private Companion() {
    }

    static Object[] EMPTY_ARRAY = new Object[0];

    static boolean isKeyword(RoundToken token, IdentifierToken keyword) {
        return token.isIdentifier() && token.getIdentifier().equalsIgnoreCase(
                keyword.getIdentifier());
    }

    static boolean isNotKeyword(RoundToken token, IdentifierToken keyword) {
        return isKeyword(token, keyword);
    }

    static String getIdentifierOrBacktick(RoundToken token) {
        if (token.isIdentifier()) {
            return token.getIdentifier();
        } else if (token instanceof StringToken && ((StringToken) token).isBacktick()) {
            return token.getValue().toString();
        } else {
            return null;
        }
    }

    static String[] getChainName(TokenInfoStream stream) {
        String name = getIdentifierOrBacktick(stream.next());
        if (name == null) stream.throwWrongSyntax();

        if (stream.next().isDot()) {
            String name2 = getIdentifierOrBacktick(stream.next());
            return new String[]{name, name2};
        } else {
            stream.previous();
            return new String[]{name};
        }
    }

    static boolean isIndexDefStartToken(RoundToken token) {
        return isKeyword(token, PRIMARY) ||
                isKeyword(token, UNIQUE) ||
                isKeyword(token, INDEX) ||
                isKeyword(token, KEY);
    }
}
