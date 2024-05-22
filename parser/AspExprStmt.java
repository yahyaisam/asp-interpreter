package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspExprStmt extends AspSmallStmt {
	AspExpr ae;

	AspExprStmt(int n) {
		super(n);
	}

    public static AspExprStmt parse(Scanner s) {
        enterParser("expr stmt");

        //-- Must be changed in part 2:
        AspExprStmt aes = new AspExprStmt(s.curLineNum());
        aes.ae = AspExpr.parse(s);

        leaveParser("expr stmt");
        return aes;
}

@Override
    public void prettyPrint() {
        ae.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue  {
        return ae.eval(curScope);
    }
}
