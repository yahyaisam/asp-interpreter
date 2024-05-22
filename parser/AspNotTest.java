package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspNotTest extends AspSyntax {
    
    AspComparison ac;
    boolean isNot = false;
   
    AspNotTest(int n) {
        super(n);
    }

    static AspNotTest parse(Scanner s) {
        enterParser("not test");
        
        AspNotTest ant = new AspNotTest(s.curLineNum());

        if (s.curToken().kind == TokenKind.notToken) {
            skip(s, TokenKind.notToken);
            ant.isNot = true;
        }
        
        ant.ac = AspComparison.parse(s);
        
        leaveParser("not test");
        return ant;
            
    }
    
    @Override
    public void prettyPrint() {
        if (isNot) prettyWrite("not ");
        ac.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        // Hentet fra forelesning uke 41, 2022
        RuntimeValue v = ac.eval(curScope);
        if (isNot) {
            v = v.evalNot(this);
        }
        return v;
    }
}
