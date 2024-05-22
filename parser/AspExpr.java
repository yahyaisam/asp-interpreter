// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspExpr extends AspSyntax {

    ArrayList<AspAndTest> andTests = new ArrayList<>();

    AspExpr(int n) {
	    super(n);
    }

    public static AspExpr parse(Scanner s) {
        enterParser("expr");

        //-- Must be changed in part 2:
        AspExpr ae = new AspExpr(s.curLineNum());
        ae.andTests.add(AspAndTest.parse(s));

        while (s.curToken().kind == TokenKind.orToken) {
            skip(s, TokenKind.orToken);
            AspAndTest aat = AspAndTest.parse(s);
            ae.andTests.add(aat);
        }
        
        leaveParser("expr");
        return ae;
    }



    @Override
    public void prettyPrint() {
        for (int i = 0; i < andTests.size(); i++) {
            if (i != 0) prettyWrite(" or ");
            andTests.get(i).prettyPrint();
        }
    }

    // Hentet fra kompendiumet
	@Override
	public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
		RuntimeValue v = andTests.get(0).eval(curScope);

		for (int i = 1; i < andTests.size(); i++) {
			if (v.getBoolValue("or operand", this))
				return v;

			v = andTests.get(i).eval(curScope);
		}

		return v;
	}
}
