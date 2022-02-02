package org.ericadb.first.syntax;

import static org.ericadb.first.syntax.Companion.getIdentifierOrBacktick;

import java.util.ArrayList;
import java.util.List;
import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.definition.ColumnDefinition;
import org.ericadb.first.sql.definition.IndexDefinition;

/**
 * @author Jerry Will
 * @version 2022-01-31
 */
public class DefAnalyzer {

    public static ColumnDefinition analyseColumnDef(TokenInfoStream stream) {
        ColumnDefinition column = new ColumnDefinition();
        return column;
    }

    public static IndexDefinition analyseIndexDef(TokenInfoStream stream) {
        IndexDefinition index = new IndexDefinition();
        String name = getIdentifierOrBacktick(stream.next());
        if (name == null) return stream.throwWrongSyntax();
        index.setName(name);
        if (!stream.next().isLeftParenthesis()) return stream.throwWrongSyntax();
        List<String> columnNames = DefAnalyzer.analyseColumnNames(stream);
        if (!stream.next().isRightParenthesis()) return stream.throwWrongSyntax();
        index.setColumnNames(columnNames);
        return index;
    }

    public static List<String> analyseColumnNames(TokenInfoStream stream) {
        List<String> columnNames = new ArrayList<>();

        return columnNames;
    }
}
