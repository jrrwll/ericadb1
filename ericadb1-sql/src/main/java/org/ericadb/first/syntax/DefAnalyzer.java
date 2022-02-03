package org.ericadb.first.syntax;

import static org.ericadb.first.lex.KeywordToken.AUTO_INCREMENT;
import static org.ericadb.first.lex.KeywordToken.CHARSET;
import static org.ericadb.first.lex.KeywordToken.COMMENT;
import static org.ericadb.first.lex.KeywordToken.DEFAULT;
import static org.ericadb.first.lex.KeywordToken.ENGINE;
import static org.ericadb.first.lex.KeywordToken.NOT;
import static org.ericadb.first.lex.KeywordToken.NULL;
import static org.ericadb.first.lex.KeywordToken.ON;
import static org.ericadb.first.lex.KeywordToken.UPDATE;
import static org.ericadb.first.syntax.Companion.getIdentifierOrBacktick;
import static org.ericadb.first.syntax.Companion.getNumber;
import static org.ericadb.first.syntax.Companion.getString;
import static org.ericadb.first.syntax.Companion.isKeyword;
import static org.ericadb.first.syntax.Companion.isNotKeyword;

import java.util.ArrayList;
import java.util.List;
import org.dreamcat.round.lex.OperatorToken;
import org.dreamcat.round.lex.RoundToken;
import org.dreamcat.round.lex.TokenInfoStream;
import org.ericadb.first.sql.definition.ColumnDefinition;
import org.ericadb.first.sql.definition.CreateTableSqlObject;
import org.ericadb.first.sql.definition.IndexDefinition;
import org.ericadb.first.sql.definition.TypeDefinition;
import org.ericadb.first.type.EType;

/**
 * @author Jerry Will
 * @version 2022-01-31
 */
class DefAnalyzer {

    static ColumnDefinition analyseColumnDef(TokenInfoStream stream) {
        ColumnDefinition column = new ColumnDefinition();
        column.setColumnName(getIdentifierOrBacktick(stream.next()));

        // type
        EType type = EType.of(stream.next().getIdentifier());
        if (type == null) return stream.throwWrongSyntax();
        TypeDefinition typeDef = new TypeDefinition();
        typeDef.setType(type);
        if (stream.next().isLeftParenthesis()) {
            List<Integer> typeArgs = new ArrayList<>();
            typeArgs.add(getNumber(stream).intValue());
            while (stream.next().isDot()) {
                typeArgs.add(getNumber(stream).intValue());
            }
            stream.previous();
            if (!stream.next().isRightParenthesis()) return stream.throwWrongSyntax();
            typeDef.setTypeArgs(typeArgs.stream().mapToInt(Number::intValue).toArray());
        } else {
            stream.previous();
        }
        column.setType(typeDef);

        int flag = 0;
        while (!stream.next().isComma()) {
            stream.previous();
            // not null
            if (isKeyword(stream.next(), NOT)) {
                if ((flag & 0b1) != 0) return stream.throwWrongSyntax();
                else flag |= 0b1;
                if (isNotKeyword(stream.next(), NULL)) return stream.throwWrongSyntax();
                column.setNotNull(true);
            }
            // null
            else if (isKeyword(stream.previous(), NULL)) {
                stream.next();
                if ((flag & 0b10) != 0) return stream.throwWrongSyntax();
                else flag |= 0b10;
            }
            // default 1, default 'some', default b'0'
            else if (isKeyword(stream.next(), DEFAULT)) {
                if ((flag & 0b100) != 0) return stream.throwWrongSyntax();
                else flag |= 0b100;
                RoundToken token = stream.next();
                if (token.isIdentifier()) {
                    String identifier = token.getIdentifier();
                    // b'1'
                    if (isKeyword(token, NULL)) {
                        column.setDefaultValue(null);
                    } else if (identifier.equalsIgnoreCase("b")) {
                        column.setDefaultValue(getString(stream));
                    } else {
                        column.setDefaultValue(identifier);
                    }
                } else if (token.isValue()) {
                    column.setDefaultValue(token.getValue()); // todo
                }
            }
            // auto_increment
            else if (isKeyword(stream.previous(), AUTO_INCREMENT)) {
                stream.next();
                if ((flag & 0b1_000) != 0) return stream.throwWrongSyntax();
                else flag |= 0b1_000;
                column.setAutoIncrement(true);
            }
            // comment 'some comment'
            else if (isKeyword(stream.next(), COMMENT)) {
                if ((flag & 0b10_000) != 0) return stream.throwWrongSyntax();
                else flag |= 0b10_000;
                column.setComment(getString(stream));
            }
            // on update
            else if (isKeyword(stream.previous(), ON)) {
                stream.next();
                if (isKeyword(stream.next(), UPDATE)) {
                    RoundToken token = stream.next();
                    if (!token.isIdentifier()) return stream.throwWrongSyntax();
                    column.setOnUpdate(token.getIdentifier());
                } else {
                    return stream.throwWrongSyntax();
                }
            } else {
                return stream.throwWrongSyntax();
            }
        }
        stream.previous();
        return column;
    }

    static IndexDefinition analyseIndexDef(TokenInfoStream stream) {
        IndexDefinition index = new IndexDefinition();
        String name = getIdentifierOrBacktick(stream.next());
        if (name == null) return stream.throwWrongSyntax();
        index.setName(name);
        if (!stream.next().isLeftParenthesis()) return stream.throwWrongSyntax();
        List<String> columnNames = analyseColumnNames(stream);
        if (!stream.next().isRightParenthesis()) return stream.throwWrongSyntax();
        index.setColumnNames(columnNames);
        return index;
    }

    static List<String> analyseColumnNames(TokenInfoStream stream) {
        List<String> columnNames = new ArrayList<>();
        String columnName = getIdentifierOrBacktick(stream.next());
        columnNames.add(columnName);
        while (stream.next().isComma()) {
            columnNames.add(getIdentifierOrBacktick(stream.next()));
        }
        stream.previous();
        return columnNames;
    }

    static void analyseTableProps(TokenInfoStream stream, CreateTableSqlObject sqlObject) {
        RoundToken token;
        while (stream.hasNext()) {
            token = stream.next();
            if (token.isSemicolon()) break;

            if (isKeyword(token, ENGINE)) {
                requireAssign(stream);
                if (stream.next().isValue()) {
                    stream.previous();
                    sqlObject.setEngine(getString(stream));
                } else if (stream.previous().isIdentifier()) {
                    sqlObject.setEngine(stream.next().getIdentifier());
                } else {
                    stream.throwWrongSyntax();
                    return;
                }
            } else if (isKeyword(token, COMMENT)) {
                requireAssign(stream);
                sqlObject.setComment(getString(stream));
            } else if (isKeyword(token, DEFAULT)) {
                if (isKeyword(stream.next(), CHARSET)) {
                    requireAssign(stream);
                    if (stream.next().isValue()) {
                        stream.previous();
                        sqlObject.setDefaultCharset(getString(stream));
                    } else if (stream.previous().isIdentifier()) {
                        sqlObject.setDefaultCharset(stream.next().getIdentifier());
                    } else {
                        stream.throwWrongSyntax();
                        return;
                    }
                } else {
                    stream.throwWrongSyntax();
                    return;
                }
            }
        }
    }

    private static void requireAssign(TokenInfoStream stream) {
        if (!stream.next().equals(OperatorToken.ASSIGN)) {
            stream.throwWrongSyntax();
        }
    }
}
