package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspFactorOpr extends AspSyntax{

    public TokenKind tkOpr;

    AspFactorOpr(int num){
        super(num);
    }

    public static AspFactorOpr parse(Scanner s){
        enterParser("factor opr");
        AspFactorOpr afo = new AspFactorOpr(s.curLineNum());

        afo.tkOpr = s.curToken().kind;
        skip(s, s.curToken().kind);

        leaveParser("factor opr");
        return afo;
    }

    @Override
    public void prettyPrint(){
        prettyWrite(" " + tkOpr.toString() + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) {
        // riktig
        return null; 
    }
}
