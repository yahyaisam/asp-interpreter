package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.main.*;
import java.util.ArrayList;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspTerm extends AspSyntax {

    ArrayList<AspFactor> factors = new ArrayList<>();
    ArrayList<AspTermOpr> termOprs = new ArrayList<>();

	AspTerm(int n) {
        super(n);
	}

    static AspTerm parse(Scanner s) {
        enterParser("term");
        
        AspTerm at = new AspTerm(s.curLineNum());
        at.factors.add(AspFactor.parse(s));
        
        while (s.curToken().kind == TokenKind.plusToken || s.curToken().kind == TokenKind.minusToken) {
            at.termOprs.add(AspTermOpr.parse(s));
            at.factors.add(AspFactor.parse(s));
        }
        
        leaveParser("term");
        return at;
    }

    @Override
    void prettyPrint() {
        factors.get(0).prettyPrint();

        if (termOprs.size() == 1) {
            termOprs.get(0).prettyPrint();
            factors.get(1).prettyPrint();
        }

        else {
            for (int i = 1; i < factors.size(); i++) {
                termOprs.get(i-1).prettyPrint();
                factors.get(i).prettyPrint();
            }	
        }	
    }
    
    // Hentet fra forelesning uke 41, 2021
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = factors.get(0).eval(curScope);

        for (int i = 1; i < factors.size(); ++i) {
            TokenKind tk;
            String k = termOprs.get(i-1).opr;
            
            if (k.equals("+")) tk = TokenKind.plusToken;
            else tk = TokenKind.minusToken;

            switch (tk) {

                case minusToken:
                    v = v.evalSubtract(factors.get(i).eval(curScope), this); break;

                case plusToken:
                    v = v.evalAdd(factors.get(i).eval(curScope), this); break;
                    
                default:
                    Main.panic("Illegal term operator: " + k + "!");
            }
        }
        return v;
    }
}
