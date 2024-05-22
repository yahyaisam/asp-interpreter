package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import java.util.ArrayList;

public class AspListDisplay extends AspAtom {
    
    ArrayList<AspExpr> expressions = new ArrayList<>();

    AspListDisplay(int n) {
        super(n);
    }
    
    static AspListDisplay parse(Scanner s) {
        enterParser("list display");
        
        AspListDisplay ald = new AspListDisplay(s.curLineNum());
        skip(s, TokenKind.leftBracketToken);

        while (s.curToken().kind != TokenKind.rightBracketToken) {
            ald.expressions.add(AspExpr.parse(s));
            if (s.curToken().kind.equals(TokenKind.commaToken)) skip(s, TokenKind.commaToken);
        }
        skip(s, TokenKind.rightBracketToken);
        leaveParser("list display");
        return ald;
        
    }

    public int listSize() {
        return expressions.size();
    }

    public ArrayList<AspExpr> getExprs() {
        return expressions;
    }

    public void runtimeSetExprs(ArrayList<AspExpr> ae) {
        this.expressions = ae;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("[");
        
        for (int i = 0; i < expressions.size(); i++) {
            if (i != 0 && i != expressions.size()) prettyWrite(",");
            expressions.get(i).prettyPrint();
        }

        prettyWrite("]");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> e = new ArrayList<>();

        for (AspExpr ae : expressions) e.add(ae.eval(curScope));

        return new RuntimeListValue(e);
    }
}
