package org.ericadb.first.lex;

import java.util.Arrays;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.round.lex.LexSettings;

/**
 * @author Jerry Will
 * @version 2022-01-08
 */
@Getter
@Setter
@Slf4j
public class SqlLexSettings extends LexSettings {

    public SqlLexSettings() {
        super();
        singleComments = Arrays.asList("--", "//");
        expressionName = "SQL";
    }

    static {
        String keywordClassName = KeywordToken.class.getName();
        if (log.isDebugEnabled()) {
            log.debug("load keyword class {}", keywordClassName);
        }
    }
}
