package org.ericadb.first.syntax;

import static org.ericadb.first.lex.KeywordToken.*;
import static org.ericadb.first.syntax.Companion.isKeyword;
import static org.ericadb.first.syntax.Companion.isNotKeyword;

import org.dreamcat.round.lex.RoundToken;
import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.SqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
public class SqlAnalyzer {

    /**
     * sql syntax analyse
     *
     * @param stream sql tokens
     * @return sql object
     */
    public static SqlObject analyse(TokenInfoStream stream) {
        SqlObject sqlObject = null;
        RoundToken token;
        while (stream.hasNext()) {
            token = stream.next();
            if (isKeyword(token, SELECT)) {
                sqlObject = SelectAnalyzer.analyse(stream);
            } else if (isKeyword(token, UPDATE)) {
                sqlObject = UpdateSetAnalyzer.analyse(stream);
            } else if (isKeyword(token, INSERT)) {
                if (isNotKeyword(stream.next(), INTO)) return stream.throwWrongSyntax();
                sqlObject = InsertIntoAnalyzer.analyse(stream);
            } else if (isKeyword(token, DELETE)) {
                if (isNotKeyword(stream.next(), FROM)) return stream.throwWrongSyntax();
                sqlObject = DeleteFromAnalyzer.analyse(stream);
            } else if (isKeyword(token, CREATE)) {
                sqlObject = analyseCreate(stream);
            } else if (isKeyword(token, ALTER)) {

            } else if (isKeyword(token, DROP)) {
                sqlObject = analyseDrop(stream);
            } else if (isKeyword(token, USE)) {

            }
        }
        return sqlObject;
    }

    static SqlObject analyseCreate(TokenInfoStream stream) {
        RoundToken token = stream.next();
        if (isKeyword(token, TABLE)) {
            return TableAnalyzer.analyseCreateTable(stream);
        } else if (isKeyword(token, INDEX)) {
            return IndexAnalyzer.analyseCreateIndex(stream);
        } else if (isKeyword(token, DATABASE)) {
            return DatabaseAnalyzer.analyseCreateDatabase(stream);
        } else if (isKeyword(token, OR)) {
            if (isNotKeyword(stream.next(), REPLACE)) return stream.throwWrongSyntax();
            token = stream.next();
            return stream.throwWrongSyntax();
        } else {
            return stream.throwWrongSyntax();
        }
    }

    static SqlObject analyseDrop(TokenInfoStream stream) {
        RoundToken token = stream.next();
        if (isKeyword(token, TABLE)) {
            return TableAnalyzer.analyseDropTable(stream);
        } else if (isKeyword(token, INDEX)) {
            return IndexAnalyzer.analyseDropIndex(stream);
        } else if (isKeyword(token, DATABASE)) {
            return DatabaseAnalyzer.analyseDropDatabase(stream);
        } else if (isKeyword(token, OR)) {
            if (isNotKeyword(stream.next(), REPLACE)) return stream.throwWrongSyntax();
            token = stream.next();
            return stream.throwWrongSyntax();
        } else {
            return stream.throwWrongSyntax();
        }
    }

}
