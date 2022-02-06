package org.ericadb.first.syntax;

import org.dreamcat.round.el.ast.ElNode;
import org.dreamcat.round.el.ast.SnippetAnalyzer;
import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.query.SelectSqlObject;

/**
 * @author Jerry Will
 * @version 2021-07-22
 */
class SelectAnalyzer {

    static SqlObject analyse(TokenInfoStream stream) {
        SelectSqlObject sqlObject = new SelectSqlObject(stream.getExpression());
        ElNode node = SnippetAnalyzer.analyse(stream);

        return sqlObject;
    }
}
