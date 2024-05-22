package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspIntegerLiteral extends AspAtom {
    long numVal;

    AspIntegerLiteral(int n) {
        super(n);
    }
    
    static AspIntegerLiteral parse(Scanner s) {
        enterParser("integer literal");
        
        AspIntegerLiteral ail = new AspIntegerLiteral(s.curLineNum());
        ail.numVal = s.curToken().integerLit;
        skip(s, TokenKind.integerToken);

        if (s.curToken().kind.equals(TokenKind.integerToken)) {
            parserError("Syntax error: Cannot have compounded integers", s.curLineNum());
        }
       
        leaveParser("integer literal");
        return ail;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("" + numVal);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        return new RuntimeIntValue(numVal);
    }
}
