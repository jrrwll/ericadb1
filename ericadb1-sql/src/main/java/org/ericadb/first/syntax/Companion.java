package org.ericadb.first.syntax;

import static org.ericadb.first.lex.KeywordToken.INDEX;
import static org.ericadb.first.lex.KeywordToken.KEY;
import static org.ericadb.first.lex.KeywordToken.PRIMARY;
import static org.ericadb.first.lex.KeywordToken.UNIQUE;

import java.util.function.Consumer;
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
        return !isKeyword(token, keyword);
    }

    static String getIdentifierOrBacktick(TokenInfoStream stream) {
        String name = getIdentifierOrBacktick(stream.next());
        if (name == null) return stream.throwWrongSyntax();
        return name;
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

    static void analyseDatabaseAndTableName(
            TokenInfoStream stream, Consumer<String> databaseSetter, Consumer<String> tableSetter) {
        String name = getIdentifierOrBacktick(stream);
        if (name == null) stream.throwWrongSyntax();

        if (stream.hasNext()) {
            if (stream.next().isDot()) {
                String name2 = getIdentifierOrBacktick(stream);
                databaseSetter.accept(name);
                tableSetter.accept(name2);
                return;
            } else {
                stream.previous();
            }
        }
        tableSetter.accept(name);
    }

    static Number getNumber(TokenInfoStream stream) {
        RoundToken token = stream.next();
        if (token.isValue()) {
            Object v = token.getValue();
            if (v instanceof Number) {
                return (Number) v;
            }
        }
        return stream.throwWrongSyntax();
    }

    static String getString(TokenInfoStream stream) {
        RoundToken token = stream.next();
        if (token.isValue()) {
            Object v = token.getValue();
            if (v instanceof String) {
                return (String) v;
            }
        }
        return stream.throwWrongSyntax();
    }

    static boolean isIndexDefStartToken(RoundToken token) {
        return isKeyword(token, PRIMARY) ||
                isKeyword(token, UNIQUE) ||
                isKeyword(token, INDEX) ||
                isKeyword(token, KEY);
    }
}
