package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspSubscription extends AspPrimarySuffix {

	AspExpr ae;

	AspSubscription(int n) {
		super(n);
	}

	static AspSubscription parse(Scanner s) {
		enterParser("subscription");

		AspSubscription as = new AspSubscription(s.curLineNum());
		
		skip(s, TokenKind.leftBracketToken);
		as.ae = AspExpr.parse(s);
		skip(s, TokenKind.rightBracketToken);
	
        leaveParser("subscription");
        return as;
	}
    
    @Override
    public void prettyPrint() {
		prettyWrite("[");
		ae.prettyPrint();
		prettyWrite("]");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        return ae.eval(curScope);
    }
}
