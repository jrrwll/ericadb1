package org.ericadb.first.syntax;

import static org.ericadb.first.lex.KeywordToken.EXISTS;
import static org.ericadb.first.lex.KeywordToken.IF;
import static org.ericadb.first.lex.KeywordToken.NOT;
import static org.ericadb.first.syntax.Companion.getIdentifierOrBacktick;
import static org.ericadb.first.syntax.Companion.isKeyword;
import static org.ericadb.first.syntax.Companion.isNotKeyword;

import org.dreamcat.round.lex.RoundToken;
import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.definition.CreateDatabaseSqlObject;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
class AlterAnalyzer {

    /**
     * alter table $table (
     *      add column ...,
     *      modify column ...,
     *      rename column
     * )
     *
     * @param stream sql tokens
     * @return sql object
     */
    static SqlObject analyseAlterTable(TokenInfoStream stream) {
        CreateDatabaseSqlObject sqlObject = new CreateDatabaseSqlObject(stream.getExpression());
        RoundToken token = stream.next();
        if (isKeyword(token, IF)) {
            if (isNotKeyword(stream.next(), NOT) && isNotKeyword(stream.next(), EXISTS)) {
                return stream.throwWrongSyntax();
            }
            sqlObject.setIfNotExists(true);
        } else {
            stream.previous();
        }
        String name = getIdentifierOrBacktick(stream);
        sqlObject.setDatabaseName(name);
        return sqlObject;
    }
}
