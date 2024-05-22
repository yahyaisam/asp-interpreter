package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspTermOpr extends AspSyntax {
    
    public static TokenKind tkOpr;
    public String opr = "";
    
    AspTermOpr(int n) {
        super(n);
    }

    static AspTermOpr parse(Scanner s) {
        enterParser("term opr");

        AspTermOpr ato = new AspTermOpr(s.curLineNum());

        switch (s.curToken().kind) {

            case plusToken:
                tkOpr = TokenKind.plusToken;
                ato.opr = "+";
                s.readNextToken();
                break;

            case minusToken:
                tkOpr = TokenKind.minusToken;
                ato.opr = "-";
                s.readNextToken();
                break;
                
            default:
                parserError("Expected an term operator token, but found a " +
                s.curToken().kind + "!", s.curLineNum());
            }

        leaveParser("term opr");
        return ato;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(opr);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Riktig
        return null;
    }
}
