package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspCompOpr extends AspSyntax {
    
    public String opr = "";
    public static TokenKind tkOpr;
    
    AspCompOpr(int n) {
        super(n);
    }

    static AspCompOpr parse(Scanner s) {
        enterParser("comp opr");

        AspCompOpr aco = new AspCompOpr(s.curLineNum());
        TokenKind tk = s.curToken().kind;

        switch (tk) {

            case lessToken:
                aco.opr = "<";
                tkOpr = tk;
                s.readNextToken();
                break;

            case greaterToken:
                aco.opr = ">";
                tkOpr = tk;
                s.readNextToken();
                break;

            case doubleEqualToken:
                aco.opr = "==";
                tkOpr = tk;
                s.readNextToken();
                break;
                
            case greaterEqualToken:
                aco.opr = ">=";
                tkOpr = tk;
                s.readNextToken();
                break;
                
            case lessEqualToken:
                aco.opr = "<=";
                tkOpr = tk;
                s.readNextToken();
                break;

            case notEqualToken:
                aco.opr = "!=";
                tkOpr = tk;
                s.readNextToken();
                break;
                
            default:
                parserError("Expected an comparison operator token, but found a " +
                s.curToken().kind + "!", s.curLineNum());
            }
            
        leaveParser("comp opr");
        return aco;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(" " + opr + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Riktig
        return null;
    }
}
