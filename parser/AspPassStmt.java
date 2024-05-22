package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspPassStmt extends AspSmallStmt {
	
	AspPassStmt(int n) {
		super(n);
	}
	
	static AspPassStmt parse(Scanner s) {
        enterParser("pass stmt");
        AspPassStmt aps = new AspPassStmt(s.curLineNum());
        skip(s, TokenKind.passToken);

        leaveParser("pass stmt");
        return aps;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("pass");
    }

   // Hentet fra forelesningen som ble holdt den 2. november 2022
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        trace("pass");        
        return null;
    }
}
