package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import java.util.ArrayList;

public class AspSuite extends AspSyntax {

    ArrayList<AspStmt> stmts = new ArrayList<>();
    AspSmallStmtList assl;
    int asslCounter = 0;

    AspSuite(int n) {
        super(n);
    }

    static AspSuite parse(Scanner s) {
        enterParser("suite");
        AspSuite as = new AspSuite(s.curLineNum());

		if (s.curToken().kind.equals(TokenKind.newLineToken)) {
			skip(s, TokenKind.newLineToken);
			skip(s, TokenKind.indentToken);

			while (!s.curToken().kind.equals(TokenKind.dedentToken)) {
				as.stmts.add(AspStmt.parse(s));
			}
			skip(s, TokenKind.dedentToken);
		}

		else {
            as.assl = AspSmallStmtList.parse(s);
            as.asslCounter = 1;
		}

        leaveParser("suite");
        return as;
    }
    
    @Override
    public void prettyPrint() {
        if (asslCounter == 1) assl.prettyPrint();

        else {
            prettyWriteLn();
            prettyIndent();

			for (int i = 0; i < stmts.size(); i++) {
                stmts.get(i).prettyPrint();
            }
            prettyDedent();
		}
     }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        if (stmts.isEmpty()) return assl.eval(curScope);
        
        RuntimeValue v = null;
        for (AspStmt s : stmts) v = s.eval(curScope);
        return v;
    }
}
