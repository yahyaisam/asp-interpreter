package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import java.util.ArrayList;

public class AspGlobalStmt extends AspSmallStmt {

	ArrayList<AspName> names = new ArrayList<>();

	AspGlobalStmt(int n) {
		super(n);
	}

    static AspGlobalStmt parse(Scanner s) {
        enterParser("global stmt");
        AspGlobalStmt ags = new AspGlobalStmt(s.curLineNum());

		skip(s, TokenKind.globalToken);
        ags.names.add(AspName.parse(s));

		while (s.curToken().kind.equals(TokenKind.commaToken)) {
			skip(s, TokenKind.commaToken);
			ags.names.add(AspName.parse(s));
		}

        leaveParser("global stmt");
        return ags;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("global ");
        for (int i = 0; i < names.size(); i++) {
            if (i != 0 && i != names.size() - 1) prettyWrite(",");
            names.get(i).prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        RuntimeValue v = null;
        
        for (int i = 0; i < names.size(); i++) {
            
            curScope.registerGlobalName(names.get(i).nameVal);
        }

        return v;
    }
}
