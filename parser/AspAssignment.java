package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import java.util.ArrayList;

class AspAssignment extends AspSmallStmt {
    AspName an;
    ArrayList<AspSubscription> as = new ArrayList<>();
	AspExpr ae;

    AspAssignment(int n) {
        super(n);
    }

    static AspAssignment parse(Scanner s) {
        enterParser("assignment");
        
        AspAssignment aa = new AspAssignment(s.curLineNum());
		aa.an = AspName.parse(s);

		while (s.curToken().kind.equals(TokenKind.leftBracketToken)) {
            
			aa.as.add(AspSubscription.parse(s));
		}
       
		skip(s, TokenKind.equalToken);
		aa.ae = AspExpr.parse(s);

        leaveParser("assignment");
        return aa;
    }
    
    @Override
    public void prettyPrint() {
        an.prettyPrint();

        for (int i = 0; i < as.size(); i++) { 
            as.get(i).prettyPrint();
        }

        prettyWrite(" = ");
        ae.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue evald = ae.eval(curScope);
        // hvis vi har en ordinaer tilorndning eks. a = ...
        if (as.isEmpty()) {
            curScope.assign(an.nameVal, evald);
            trace(an.nameVal + " = " + evald.showInfo());
        }

        // vi har en liste indeksert tilordning eks. a[0] = ...
        else {
            RuntimeValue n = an.eval(curScope);

            for (int i = 0 ; i < as.size()-1 ; i ++) {
                RuntimeValue v = as.get(i).eval(curScope);
                n = n.evalSubscription(v, this);
            }
            
            // because we do not reach the last sub
            AspSubscription lastSub = as.get(as.size()-1);
            RuntimeValue pos = lastSub.eval(curScope);

            // showInfo tar med anforselstegnene
            trace(an.nameVal + "[" + pos.toString() + "] = " + evald.showInfo());
            n.evalAssignElem(pos, evald, this);

        }

        return null;
    }
}
