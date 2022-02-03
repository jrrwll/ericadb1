package org.ericadb.first.syntax;


import static org.ericadb.first.lex.KeywordToken.EXISTS;
import static org.ericadb.first.lex.KeywordToken.IF;
import static org.ericadb.first.lex.KeywordToken.INDEX;
import static org.ericadb.first.lex.KeywordToken.KEY;
import static org.ericadb.first.lex.KeywordToken.NOT;
import static org.ericadb.first.lex.KeywordToken.PRIMARY;
import static org.ericadb.first.lex.KeywordToken.UNIQUE;
import static org.ericadb.first.syntax.Companion.getIdentifierOrBacktick;
import static org.ericadb.first.syntax.Companion.isIndexDefStartToken;
import static org.ericadb.first.syntax.Companion.isKeyword;
import static org.ericadb.first.syntax.Companion.isNotKeyword;

import java.util.List;
import org.dreamcat.round.lex.RoundToken;
import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.SqlObject;
import org.ericadb.first.sql.definition.ColumnDefinition;
import org.ericadb.first.sql.definition.CreateTableSqlObject;
import org.ericadb.first.sql.definition.IndexDefinition;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
class IndexAnalyzer {

    static SqlObject analyseCreateIndex(TokenInfoStream stream) {
        return null;
    }

    static SqlObject analyseDropIndex(TokenInfoStream stream) {
        return null;
    }
}
