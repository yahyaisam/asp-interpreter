package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import java.util.ArrayList;

public class AspIfStmt extends AspCompoundStmt {

    ArrayList<AspExpr> expressions = new ArrayList<>();
    ArrayList<AspSuite> suites = new ArrayList<>();
    
    // Brukes for å ha kontroll på prettyprinten
    AspSuite aspSuiteElse = null;

    AspIfStmt(int n) {
        super(n);
    }
    
    static AspIfStmt parse(Scanner s) {
        enterParser("if stmt");
        AspIfStmt ais = new AspIfStmt(s.curLineNum());

		skip(s, TokenKind.ifToken);
		ais.expressions.add(AspExpr.parse(s));
		skip(s, TokenKind.colonToken);
		ais.suites.add(AspSuite.parse(s));

		while (s.curToken().kind.equals(TokenKind.elifToken)) {
			skip(s, TokenKind.ifToken);
			ais.expressions.add(AspExpr.parse(s));
			skip(s, TokenKind.colonToken);
			ais.suites.add(AspSuite.parse(s));

		}

		if (s.curToken().kind.equals(TokenKind.elseToken)) {
            skip(s, TokenKind.elseToken);
            skip(s, TokenKind.colonToken);
            ais.aspSuiteElse = AspSuite.parse(s);
		}

        leaveParser("if stmt");
        return ais;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("if ");

		expressions.get(0).prettyPrint();
        prettyWrite(": ");
        suites.get(0).prettyPrint();

        for (int i = 1; i < suites.size(); i++) {
            prettyWrite("elif ");
            expressions.get(i).prettyPrint();
            prettyWrite(": ");
            suites.get(i).prettyPrint();
        }

        if (aspSuiteElse != null) {
            prettyWrite("else");
            prettyWrite(": ");
            aspSuiteElse.prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        RuntimeValue v = expressions.get(0).eval(curScope);
        boolean isBreak = false;

        if (v.getBoolValue("if test", this)) {
            trace("if True");
            suites.get(0).eval(curScope);

            return new RuntimeBoolValue(true);
        }
        
        else {
            trace("if False");
        
            for (int i = 1; i < expressions.size(); i++) {
                trace("elif ...");
                v = expressions.get(i).eval(curScope);

                if (v.getBoolValue("elif test", this)) {
                    suites.get(i-1).eval(curScope);

                    return new RuntimeBoolValue(true);
                }
              
            }
            if (aspSuiteElse != null) {
                trace("else True");
                aspSuiteElse.eval(curScope);

                return new RuntimeBoolValue(true);
            }
        }
        
        trace("if False");
        return new RuntimeBoolValue(false);
    }
}
