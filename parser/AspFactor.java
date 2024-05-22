package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.main.Main;


class AspFactor extends AspSyntax {

    ArrayList<AspFactorPrefix> factorPrefixes = new ArrayList<>();
    ArrayList<AspFactorOpr> factorOprs = new ArrayList<>();
    ArrayList<AspPrimary> primaries = new ArrayList<>();
    
    AspFactor(int n) {
        super(n);
    }

    static AspFactor parse(Scanner s) {
        enterParser("factor");
        AspFactor af = new AspFactor(s.curLineNum());
        

        if (s.isFactorPrefix()) af.factorPrefixes.add(AspFactorPrefix.parse(s));
        else af.factorPrefixes.add(null);

        af.primaries.add(AspPrimary.parse(s));

        while (s.isFactorOpr()) {
            af.factorOprs.add(AspFactorOpr.parse(s));

            if (s.isFactorPrefix()) af.factorPrefixes.add(AspFactorPrefix.parse(s));
            else af.factorPrefixes.add(null);

            af.primaries.add(AspPrimary.parse(s));
        }
         
       leaveParser("factor");
       return af;
    }

    @Override
    public void prettyPrint() {
        
        if (factorPrefixes.get(0) !=null) factorPrefixes.get(0).prettyPrint();

        primaries.get(0).prettyPrint();
        
        for (int i = 1; i < primaries.size(); i++) {

            factorOprs.get(i - 1).prettyPrint();
            
            if (factorPrefixes.get(i) != null) {
                factorPrefixes.get(i).prettyPrint();
            }

            primaries.get(i).prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = primaries.get(0).eval(curScope);


            if (factorPrefixes.get(0) != null) {

                TokenKind tk = factorPrefixes.get(0).tkPre;

                switch (tk) {

                    case plusToken:
                        v = v.evalPositive(this);
                        break;

                    case minusToken:
                        v = v.evalNegate(this);
                        break;

                    default:
                        Main.panic("Illegal factor operator" + tk);
                }
            }
        
            for (int i = 1; i < primaries.size(); i++) {

                TokenKind tk = factorOprs.get(i-1).tkOpr;
                RuntimeValue temp = primaries.get(i).eval(curScope);

                if (factorPrefixes.get(i) != null) {

                    TokenKind tokenK = factorPrefixes.get(i).tkPre;

                    switch (tokenK) {

                        case plusToken:
                            v = v.evalPositive(this);
                            break;

                        case minusToken:
                            v = v.evalNegate(this);
                            break;

                        default:
                            Main.panic("Illegal factor operator" + tk);
                    }
                }
                switch (tk) {

                    
                    case percentToken:
                        v = v.evalModulo(temp, this);
                        break;

                    case slashToken:
                        v = v.evalDivide(temp, this);
                        break;

                    case doubleSlashToken:
                        v = v.evalIntDivide(temp, this);
                        break;

                    case astToken:
                        v = v.evalMultiply(temp, this);
                        break;

                    default:
                        Main.panic("illefal factor operator " + tk);
                }
            }
        return v;
    }
}
