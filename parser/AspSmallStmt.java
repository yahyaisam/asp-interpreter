package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;

public abstract class AspSmallStmt extends AspSyntax {

	AspSmallStmt(int n) {
        super(n);
    }
    
    static AspSmallStmt parse(Scanner s) {
		enterParser("small stmt");

        AspSmallStmt ass = null;
			
		if (s.curToken().kind.equals(TokenKind.globalToken)) ass = AspGlobalStmt.parse(s);

		else if (s.curToken().kind.equals(TokenKind.passToken)) ass = AspPassStmt.parse(s);

		else if(s.curToken().kind.equals(TokenKind.returnToken)) ass = AspReturnStmt.parse(s);

        else if (s.anyEqualToken()) ass = AspAssignment.parse(s);

        else {
            ass = AspExprStmt.parse(s);
        }

		leaveParser("small stmt");
        return ass;
    }
}
