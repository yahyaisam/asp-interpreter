package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;

class AspFactorPrefix extends AspSyntax {
    
    String opr = "";
    public static TokenKind tkPre;
    
    AspFactorPrefix(int n) {
        super(n);
    }

    static AspFactorPrefix parse(Scanner s) {
        enterParser("factor prefix");
        AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum());

        switch (s.curToken().kind) {
            case plusToken:
                afp.opr = "+";
                tkPre = TokenKind.plusToken;
                s.readNextToken();
                break;

            case minusToken:
                afp.opr = "-";
                tkPre = TokenKind.minusToken;
                s.readNextToken();
                break;
                
            default:
                parserError("Expected an factor prefix token, but found a " +
                s.curToken().kind + "!", s.curLineNum());
            }

        leaveParser("factor prefix");
        return afp;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(" " + opr + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {        
        // riktig
        return null;
    }
}
