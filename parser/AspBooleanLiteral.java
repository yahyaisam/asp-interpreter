package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspBooleanLiteral extends AspAtom {
    TokenKind b;

    AspBooleanLiteral(int n) {
        super(n);
    }
    static AspBooleanLiteral parse(Scanner s) {
        enterParser("boolean literal");
        
        AspBooleanLiteral bool = new AspBooleanLiteral(s.curLineNum());
        bool.b = s.curToken().kind;
        s.readNextToken();

        leaveParser("boolean literal");
        return bool;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("" + b);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        if (b.equals(TokenKind.falseToken)) return new RuntimeBoolValue(false);
    
        return new RuntimeBoolValue(true);
    }
}
