package org.ericadb.first.sql.el;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.dreamcat.round.el.ast.ElNode;

/**
 * @author Jerry Will
 * @version 2022-02-03
 */
@Getter
public class ElObject {

    private final List<ElObject> children = new ArrayList<>();

    public ElObject of(ElNode root) {
        System.out.println(root); // todo impl
    }
}
