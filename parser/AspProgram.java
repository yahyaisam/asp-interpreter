// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.ArrayList;

public class AspProgram extends AspSyntax {

    //-- Must be changed in part 2:
    ArrayList<AspStmt> stmts = new ArrayList<>();

    AspProgram(int n) {
	    super(n);
    }

    public static AspProgram parse(Scanner s) {
        enterParser("program");

        AspProgram ap = new AspProgram(s.curLineNum());
        while (s.curToken().kind != eofToken) {
            
            //-- Must be changed in part 2:
            ap.stmts.add(AspStmt.parse(s));
        }

        leaveParser("program");
        return ap;
    }


    @Override
    public void prettyPrint() {
		
	    //-- Must be changed in part 2:
        for (int i = 0; i < stmts.size(); i++) {
            stmts.get(i).prettyPrint();
        }
    }

    // Hentet fra forelesningen som ble holdt den 9. november
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        for (AspStmt as: stmts) {
            try {
                as.eval(curScope);
            } catch (RuntimeReturnValue rrv) {
                RuntimeValue.runtimeError("Return statement outside function!", rrv.lineNum);
            }
        }
        return null;
    }
}
