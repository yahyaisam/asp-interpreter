package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspFloatLiteral extends AspAtom {
    
    float floatLit;

    AspFloatLiteral(int n) {
        super(n);
    }
    
    static AspFloatLiteral parse(Scanner s) {
        enterParser("float literal");

        AspFloatLiteral afl = new AspFloatLiteral(s.curLineNum());
        afl.floatLit = (float) s.curToken().floatLit;
        skip(s, TokenKind.floatToken);
        
        leaveParser("float literal");

        return afl;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("" + floatLit);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        
        return new RuntimeFloatValue(floatLit);
    }
}
