package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspWhileStmt extends AspCompoundStmt {
    AspExpr ae;
    AspSuite as;

    AspWhileStmt(int n) {
        super(n);
    }

    static AspWhileStmt parse(Scanner s) {
        enterParser("while stmt");

        AspWhileStmt aws = new AspWhileStmt(s.curLineNum());
        skip(s, TokenKind.whileToken);
        aws.ae = AspExpr.parse(s);
        skip(s, TokenKind.colonToken);
        aws.as = AspSuite.parse(s);

        leaveParser("while stmt");
        return aws;
    }
    
    @Override
    public void prettyPrint() {
        prettyWrite("while ");
        ae.prettyPrint();

        prettyWrite(":");
        as.prettyPrint();
    }

    // Hentet fra forelesningen som var 2. november 2022
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        while (true) {
            RuntimeValue t = ae.eval(curScope);
      
            if (! t.getBoolValue("while loop test",this)) break;
            trace("while True: ...");
      
            as.eval(curScope);
        }
        trace("while False:");
        return null;
    }
}
