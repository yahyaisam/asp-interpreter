package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;


class AspName extends AspAtom {
    String nameVal;

    AspName(int n) {
        super(n);
    }
    
    static AspName parse(Scanner s) {
        enterParser("name");
     
        AspName an = new AspName(s.curLineNum());

        an.nameVal = s.curToken().name;
        skip(s, TokenKind.nameToken);
        leaveParser("name");

        return an;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(nameVal);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return curScope.find(nameVal, this);
    }
}
