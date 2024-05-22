package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import java.util.ArrayList;

public class AspFuncDef extends AspCompoundStmt {
    
    ArrayList<AspName> names = new ArrayList<>();
    AspSuite as;

    AspFuncDef(int n) {
        super(n);
    }

    static AspFuncDef parse(Scanner s) {
        enterParser("func def");
        AspFuncDef afd = new AspFuncDef(s.curLineNum());

        skip(s, TokenKind.defToken);
		afd.names.add(AspName.parse(s));
		skip(s, TokenKind.leftParToken);

		if (!s.curToken().kind.equals(TokenKind.rightParToken)) {
			afd.names.add(AspName.parse(s));

			while (s.curToken().kind.equals(TokenKind.commaToken)) {
				skip(s, TokenKind.commaToken);
				afd.names.add(AspName.parse(s));
			}
		}
        
		skip(s, TokenKind.rightParToken);
		skip(s, TokenKind.colonToken);
		afd.as = AspSuite.parse(s);

        leaveParser("func def");
        return afd;
  
    }
    
    @Override
    public void prettyPrint() {
        prettyWrite("def ");
        names.get(0).prettyPrint();
        prettyWrite("(");

        for (int i = 1; i < names.size(); i++) {
            names.get(i).prettyPrint();
            if (i != names.size() - 1) prettyWrite(",");
        }
        
        prettyWrite(")");
        prettyWrite(":");
        as.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        
        ArrayList<RuntimeValue> args = new ArrayList<>();
        RuntimeStringValue v = new RuntimeStringValue(names.get(0).nameVal);
        RuntimeValue s;
 
        for (int i = 1; i < names.size(); i++) {
            args.add(new RuntimeStringValue(names.get(i).nameVal));
        }
        
        RuntimeFunc rf = new RuntimeFunc(v, args, as, curScope);


        curScope.assign(names.get(0).nameVal, rf);

        trace("def: " + rf.showInfo());

        return null;
    }
}
