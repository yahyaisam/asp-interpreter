package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspNoneLiteral extends AspAtom {

    TokenKind aspNoneVal;

    AspNoneLiteral(int n) {
        super(n);
    }
    
    static AspNoneLiteral parse(Scanner s) {
        enterParser("none literal");
        AspNoneLiteral anl = new AspNoneLiteral(s.curLineNum());
        anl.aspNoneVal = s.curToken().kind;
        skip(s, anl.aspNoneVal);
        leaveParser("none literal");

        return anl;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("" + aspNoneVal);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        trace("None");
        return new RuntimeNoneValue();
    }
}
