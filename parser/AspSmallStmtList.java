package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import java.util.ArrayList;

class AspSmallStmtList extends AspStmt {

    ArrayList<AspSmallStmt> smallstmts = new ArrayList<>();
    boolean isSemi = false;

    AspSmallStmtList(int n) {
        super(n);
    }

    static AspSmallStmtList parse(Scanner s) {
        enterParser("small stmt list");

        AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum());
        assl.smallstmts.add(AspSmallStmt.parse(s));

		while (s.curToken().kind.equals(TokenKind.semicolonToken)) {
			skip(s, TokenKind.semicolonToken);

			if (!s.curToken().kind.equals(TokenKind.newLineToken)) {
				assl.smallstmts.add(AspSmallStmt.parse(s));
			}
            else assl.isSemi = true;
		}

		skip(s, TokenKind.newLineToken);

        leaveParser("small stmt list");
        return assl;
  
    }
    
    @Override
    public void prettyPrint() {
		for (int i = 0; i < smallstmts.size(); i++) {
            if (((i == 1 ) || (i == smallstmts.size()-1)) && (smallstmts.size() != 1) )  prettyWrite("; ");
            smallstmts.get(i).prettyPrint();
        }
        
        if (isSemi) prettyWrite("; ");
        prettyWriteLn();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        RuntimeValue v = smallstmts.get(0).eval(curScope);

        for (int i = 1 ; i < smallstmts.size() ; i ++) {
            v = smallstmts.get(i).eval(curScope);
        }
        return v;
    }
}
