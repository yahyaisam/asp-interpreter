package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspReturnStmt extends AspSmallStmt {
    AspExpr ae;

	AspReturnStmt(int n) {
		super(n);
	}
	
	static AspReturnStmt parse(Scanner s) {
        enterParser("return stmt");
        AspReturnStmt ars = new AspReturnStmt(s.curLineNum());

        skip(s, TokenKind.returnToken);
        ars.ae = AspExpr.parse(s);


        leaveParser("return stmt");
        return ars;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("return ");
        ae.prettyPrint();
    }

    // Part 4
    // Hentet fra forelesningen som ble holdt den 9. november 2022
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = ae.eval(curScope);
        trace("return "+v.showInfo());
        throw new RuntimeReturnValue(v,lineNum);
        }
    }
