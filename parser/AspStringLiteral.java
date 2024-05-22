package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspStringLiteral extends AspAtom {

    String sLit;
    static Token ct;

    AspStringLiteral(int n) {
        super(n);
    }
    
    static AspStringLiteral parse(Scanner s) {
        ct = s.curToken();
        enterParser("string literal");
        
        AspStringLiteral sl = new AspStringLiteral(s.curLineNum());
        sl.sLit = s.curToken().stringLit;
        skip(s, TokenKind.stringToken);

        leaveParser("string literal");
        return sl;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(sLit);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        return new RuntimeStringValue(sLit);
    }
}
