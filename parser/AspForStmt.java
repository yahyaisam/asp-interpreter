package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.main.*;
import java.util.ArrayList;

class AspForStmt extends AspCompoundStmt {
    AspName an;
    AspExpr ae;
    AspSuite as;

    AspForStmt(int n) {
        super(n);
    }
    
    static AspForStmt parse(Scanner s) {
        enterParser("for stmt");
        AspForStmt afs = new AspForStmt(s.curLineNum());

		skip(s, TokenKind.forToken);
		afs.an = AspName.parse(s);
		skip(s, TokenKind.inToken);
		afs.ae = AspExpr.parse(s);
		skip(s, TokenKind.colonToken);
        afs.as = AspSuite.parse(s);

        leaveParser("for stmt");
        return afs;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("for ");
		an.prettyPrint();

        prettyWrite(" in ");
		ae.prettyPrint();
        
        prettyWrite(":");
		as.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        RuntimeValue v = ae.eval(curScope);

        if (v instanceof RuntimeListValue) {

            ArrayList<RuntimeValue> exprList = v.getListElems(this);

        for (int i = 0; i < exprList.size(); i++) {
            trace("for #" + (i + 1) +": " + an.nameVal + " = " + exprList.get(i).showInfo());
            curScope.assign(an.nameVal, exprList.get(i));
            v = ae.eval(curScope);
            as.eval(curScope);
        }

        }
        else if (v instanceof RuntimeStringValue) {
            String exprTxt = v.getStringValue("subscription", this);

            for (int e = 0 ; e < exprTxt.length() - 1 ; e ++) {
                trace("for #" + (e + 1) + ": " + an.nameVal + " = " + exprTxt.charAt(e));
                curScope.assign(an.nameVal, new RuntimeStringValue(exprTxt.charAt(e) + ""));
            }
        }

        else { Main.panic("runtimeError: forStmt is not iterable");}

        return v;
    }
}
