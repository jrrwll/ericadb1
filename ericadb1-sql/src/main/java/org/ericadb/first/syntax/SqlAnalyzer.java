package org.ericadb.first.syntax;

import static org.ericadb.first.lex.KeywordToken.ALTER;
import static org.ericadb.first.lex.KeywordToken.CREATE;
import static org.ericadb.first.lex.KeywordToken.DATABASE;
import static org.ericadb.first.lex.KeywordToken.DELETE;
import static org.ericadb.first.lex.KeywordToken.DROP;
import static org.ericadb.first.lex.KeywordToken.FROM;
import static org.ericadb.first.lex.KeywordToken.FUNCTION;
import static org.ericadb.first.lex.KeywordToken.INDEX;
import static org.ericadb.first.lex.KeywordToken.INSERT;
import static org.ericadb.first.lex.KeywordToken.INTO;
import static org.ericadb.first.lex.KeywordToken.SELECT;
import static org.ericadb.first.lex.KeywordToken.TABLE;
import static org.ericadb.first.lex.KeywordToken.TRUNCATE;
import static org.ericadb.first.lex.KeywordToken.UNIQUE;
import static org.ericadb.first.lex.KeywordToken.UPDATE;
import static org.ericadb.first.lex.KeywordToken.USE;
import static org.ericadb.first.lex.KeywordToken.VIEW;
import static org.ericadb.first.syntax.Companion.isKeyword;
import static org.ericadb.first.syntax.Companion.isNotKeyword;

import org.dreamcat.round.lex.RoundToken;
import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.definition.CreateIndexSqlObject;

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
        RoundToken token = stream.next();
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
            sqlObject = AlterAnalyzer.analyseAlterTable(stream);
        } else if (isKeyword(token, DROP)) {
            sqlObject = analyseDrop(stream);
        } else if (isKeyword(token, TRUNCATE)) {
            sqlObject = OtherAnalyzer.analyseTruncate(stream);
        } else if (isKeyword(token, USE)) {
            sqlObject = OtherAnalyzer.analyseUse(stream);
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
        } else if (isKeyword(token, UNIQUE)) {
            if (isNotKeyword(stream.next(), INDEX)) return stream.throwWrongSyntax();
            CreateIndexSqlObject sqlObject = IndexAnalyzer.analyseCreateIndex(stream);
            sqlObject.setUnique(true);
            return sqlObject;
        } else if (isKeyword(token, FUNCTION)) {
            return FunctionAnalyzer.analyseCreateFunction(stream);
        } else if (isKeyword(token, VIEW)) {
            return ViewAnalyzer.analyseCreateView(stream);
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
        } else if (isKeyword(token, FUNCTION)) {
            return FunctionAnalyzer.analyseDropFunction(stream);
        } else if (isKeyword(token, VIEW)) {
            return ViewAnalyzer.analyseDropView(stream);
        } else {
            return stream.throwWrongSyntax();
        }
    }

}
