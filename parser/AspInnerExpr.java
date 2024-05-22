package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspInnerExpr extends AspAtom {

	AspExpr ae;

	AspInnerExpr(int n) {
		super(n);
	}

	static AspInnerExpr parse(Scanner s) {
		enterParser("inner expr");

		AspInnerExpr aie = new AspInnerExpr(s.curLineNum());
		skip(s, TokenKind.leftParToken);
		aie.ae = AspExpr.parse(s);
		skip(s, TokenKind.rightParToken);
	
        leaveParser("inner expr");
        return aie;
	}
    
    @Override
    public void prettyPrint() {
		prettyWrite("(");
		ae.prettyPrint();
		prettyWrite(")");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        return ae.eval(curScope);
    }
}
