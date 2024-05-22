package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import java.util.ArrayList;
import no.uio.ifi.asp.main.Main;

class AspComparison extends AspSyntax {
    ArrayList<AspCompOpr> compOprs = new ArrayList<>();
    ArrayList<AspTerm> terms = new ArrayList<>();

    AspComparison(int n) {
        super(n);
    }

    static AspComparison parse(Scanner s) {

        enterParser("comparison");
        AspComparison ac = new AspComparison(s.curLineNum());

        while (true) {
            ac.terms.add(AspTerm.parse(s));
            if (!s.isCompOpr()) break;
            
            ac.compOprs.add(AspCompOpr.parse(s));
        }

        leaveParser("comparison");
        return ac;
    }

    // Oppsettet inspirert av løsningsforslaget fra ukesoppgave 39, høsten 2021
    @Override
    public void prettyPrint() {

        for (int i = 0 ; i < terms.size() ; i ++) {
            terms.get(i).prettyPrint();
            if (i < compOprs.size()) compOprs.get(i).prettyPrint();
        }
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {    

    RuntimeValue v = terms.get(0).eval(curScope);
    

        for (int i = 1; i < terms.size(); i++) {

            if (i != 1) v = terms.get(i-1).eval(curScope);

            String tkString = compOprs.get(i-1).opr;


            RuntimeValue e = terms.get(i).eval(curScope);

            switch (tkString) {

                case "<":
                    v = v.evalLess(e, this); break;
    
                case ">":
                    v = v.evalGreater(e, this); break;
                    
                case "==":
                    v = v.evalEqual(e, this); break;
    
                case ">=":
                    v = v.evalGreaterEqual(e, this); break;
                    
                case "<=":
                    v = v.evalLessEqual(e, this); break;
                    
                case "!=":
                    v = v.evalNotEqual(e, this); break;
                    
                default:
                    Main.panic("Illegal term operator: " + tkString + "!");
            }
            if (!v.getBoolValue("term", this)) return v;
        }
        return v;
    }
}
