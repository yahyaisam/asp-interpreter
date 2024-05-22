package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;

public abstract class AspStmt extends AspSyntax {

	AspStmt(int n) {
        super(n);
    }

    static AspStmt parse(Scanner s) {
        enterParser("stmt");

        AspStmt as = null;

        // Enten smallStmtList eller CompundStmtList
        if (s.isCompStmt()) as = AspCompoundStmt.parse(s);
        else as = AspSmallStmtList.parse(s);

        leaveParser("stmt");
        return as;
    } 
}
