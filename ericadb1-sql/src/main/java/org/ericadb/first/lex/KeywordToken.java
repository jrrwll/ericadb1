package org.ericadb.first.lex;

import org.dreamcat.round.lex.IdentifierToken;

/**
 * @author Jerry Will
 * @version 2022-01-26
 */
public interface KeywordToken {
    IdentifierToken ADD = IdentifierToken.addKeywords("add", "ADD");
    IdentifierToken ALL = IdentifierToken.addKeywords("all", "ALL");
    IdentifierToken ALTER = IdentifierToken.addKeywords("alter", "ALTER");
    IdentifierToken AND = IdentifierToken.addKeywords("and", "AND");
    IdentifierToken ANY = IdentifierToken.addKeywords("any", "ANY");
    IdentifierToken AS = IdentifierToken.addKeywords("as", "AS");
    IdentifierToken ASC = IdentifierToken.addKeywords("asc", "ASC");
    IdentifierToken AUTO_INCREMENT = IdentifierToken.addKeywords("auto_increment", "AUTO_INCREMENT");

    IdentifierToken BETWEEN = IdentifierToken.addKeywords("between", "BETWEEN");
    IdentifierToken BY = IdentifierToken.addKeywords("by", "BY");

    IdentifierToken CASE = IdentifierToken.addKeywords("case", "CASE");
    IdentifierToken CHARSET = IdentifierToken.addKeywords("charset", "CHARSET");
    IdentifierToken COMMENT = IdentifierToken.addKeywords("comment", "COMMENT");
    IdentifierToken COLUMN = IdentifierToken.addKeywords("column", "COLUMN");
    IdentifierToken CREATE = IdentifierToken.addKeywords("create", "CREATE");
    IdentifierToken CROSS = IdentifierToken.addKeywords("cross", "CROSS");

    IdentifierToken DATABASE = IdentifierToken.addKeywords("database", "DATABASE");
    IdentifierToken DEFAULT = IdentifierToken.addKeywords("default", "DEFAULT");
    IdentifierToken DELETE = IdentifierToken.addKeywords("delete", "DELETE");
    IdentifierToken DESC = IdentifierToken.addKeywords("desc", "DESC");
    IdentifierToken DISTINCT = IdentifierToken.addKeywords("distinct", "DISTINCT");
    IdentifierToken DROP = IdentifierToken.addKeywords("drop", "DROP");

    IdentifierToken ELSE = IdentifierToken.addKeywords("else", "ELSE");
    IdentifierToken ENGINE = IdentifierToken.addKeywords("engine", "ENGINE");
    IdentifierToken EXISTS = IdentifierToken.addKeywords("exists", "EXISTS");

    IdentifierToken FROM = IdentifierToken.addKeywords("from", "FROM");
    IdentifierToken FUNCTION = IdentifierToken.addKeywords("function", "FUNCTION");

    IdentifierToken GROUP = IdentifierToken.addKeywords("group", "GROUP");

    IdentifierToken HAVING = IdentifierToken.addKeywords("having", "HAVING");

    IdentifierToken IF = IdentifierToken.addKeywords("if", "IF");
    IdentifierToken IN = IdentifierToken.addKeywords("in", "IN");
    IdentifierToken INDEX = IdentifierToken.addKeywords("index", "INDEX");
    IdentifierToken INNER = IdentifierToken.addKeywords("inner", "INNER");
    IdentifierToken INSERT = IdentifierToken.addKeywords("insert", "INSERT");
    IdentifierToken INTO = IdentifierToken.addKeywords("into", "INTO");

    IdentifierToken IS = IdentifierToken.addKeywords("is", "IS");

    IdentifierToken JOIN = IdentifierToken.addKeywords("join", "JOIN");

    IdentifierToken KEY = IdentifierToken.addKeywords("key", "KEY");

    IdentifierToken LEFT = IdentifierToken.addKeywords("left", "LEFT");
    IdentifierToken LIKE = IdentifierToken.addKeywords("like", "LIKE");
    IdentifierToken LIMIT = IdentifierToken.addKeywords("limit", "LIMIT");

    IdentifierToken MODIFY = IdentifierToken.addKeywords("modify", "MODIFY");

    IdentifierToken TABLE = IdentifierToken.addKeywords("table", "TABLE");

    IdentifierToken NOT = IdentifierToken.addKeywords("not", "NOT");
    IdentifierToken NULL = IdentifierToken.addKeywords("null", "NULL");

    IdentifierToken OFFSET = IdentifierToken.addKeywords("offset", "OFFSET");
    IdentifierToken ON = IdentifierToken.addKeywords("on", "ON");
    IdentifierToken OR = IdentifierToken.addKeywords("or", "OR");
    IdentifierToken ORDER = IdentifierToken.addKeywords("order", "ORDER");
    IdentifierToken OVERWRITE = IdentifierToken.addKeywords("overwrite", "OVERWRITE");

    IdentifierToken PRIMARY = IdentifierToken.addKeywords("primary", "PRIMARY");

    IdentifierToken REPLACE = IdentifierToken.addKeywords("replace", "REPLACE");
    IdentifierToken RIGHT = IdentifierToken.addKeywords("right", "RIGHT");

    IdentifierToken SELECT = IdentifierToken.addKeywords("select", "SELECT");

    IdentifierToken TRUE = IdentifierToken.addKeywords("true", "TRUE");
    IdentifierToken TRUNCATE = IdentifierToken.addKeywords("truncate", "TRUNCATE");

    IdentifierToken UNION = IdentifierToken.addKeywords("union", "UNION");
    IdentifierToken UNIQUE = IdentifierToken.addKeywords("unique", "UNIQUE");
    IdentifierToken UPDATE = IdentifierToken.addKeywords("update", "UPDATE");
    IdentifierToken USE = IdentifierToken.addKeywords("use", "USE");

    IdentifierToken VALUES = IdentifierToken.addKeywords("values", "VALUES");
    IdentifierToken VIEW = IdentifierToken.addKeywords("view", "VIEW");

    IdentifierToken WHEN = IdentifierToken.addKeywords("when", "WHEN");
    IdentifierToken WHERE = IdentifierToken.addKeywords("where", "WHERE");
}
