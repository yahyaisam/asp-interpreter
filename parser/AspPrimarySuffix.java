package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspPrimarySuffix extends AspSyntax {
    AspArguments aa;
    AspSubscription as;
    public static TokenKind tkOpr;
    boolean isSub = false;

    private boolean ifArguments = false;

    AspPrimarySuffix(int n) {
        super(n);
    }

    static AspPrimarySuffix parse(Scanner s) {

        enterParser("primary suffix");
        AspPrimarySuffix aps = new AspPrimarySuffix(s.curLineNum());
            
    
        if (s.curToken().kind.equals(TokenKind.leftParToken)) {
            tkOpr = TokenKind.leftParToken;
            aps.aa = AspArguments.parse(s);
            aps.ifArguments = true;
        }
        
        else {
            aps.as = AspSubscription.parse(s);
            tkOpr = TokenKind.leftBracketToken;
            aps.isSub = true;
        }

        leaveParser("primary suffix");
        return aps;
    }

    public boolean getType() {
        return isSub;
    }

    @Override
    public void prettyPrint() {
        if (ifArguments) aa.prettyPrint();
        else as.prettyPrint();
    }
    
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        RuntimeValue v;

        if (aa != null) {
            v = aa.eval(curScope);
            return v;
        }

        v = as.eval(curScope);
        return v;
    }
}
