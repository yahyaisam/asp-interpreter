package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import java.util.ArrayList;

class AspArguments extends AspPrimarySuffix {
    
    ArrayList<AspExpr> expressions = new ArrayList<>();

    AspArguments(int n) {
        super(n);
    }
    
    static AspArguments parse(Scanner s) {
        enterParser("arguments");
        
        AspArguments aa = new AspArguments(s.curLineNum());
        
        skip(s, TokenKind.leftParToken);

        if (!s.curToken().kind.equals(TokenKind.rightParToken)) {
            AspExpr ae = AspExpr.parse(s);
            aa.expressions.add(ae);

            while (s.curToken().kind == TokenKind.commaToken) {
                skip(s, TokenKind.commaToken);
                ae = AspExpr.parse(s); 
                aa.expressions.add(ae);
            }
        }

        skip(s, TokenKind.rightParToken);
        leaveParser("arguments");

        return aa;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("(");

        for (int i = 0; i < expressions.size(); i++) {
            if (i != 0 && i != expressions.size() - 2) prettyWrite(",");
            expressions.get(i).prettyPrint();
        }
        
        prettyWrite(")");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        ArrayList<RuntimeValue> result = new ArrayList<>();
        for (AspExpr e: expressions) { result.add(e.eval(curScope)); }
        return new RuntimeListValue(result);
    }
}
