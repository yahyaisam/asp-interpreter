package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AspDictDisplay extends AspAtom {
    
    ArrayList<AspExpr> exprs = new ArrayList<>();
    ArrayList<AspStringLiteral> stringLits = new ArrayList<>();

    public HashMap<RuntimeValue, RuntimeValue> runtimeElements = new HashMap<>();

    public AspDictDisplay(int n) {
        super(n);
    }
    
    static AspDictDisplay parse(Scanner s) {
        enterParser("dict display");

        AspDictDisplay aDictd = new AspDictDisplay(s.curLineNum());
        skip(s, TokenKind.leftBraceToken);
        boolean leftBraceSkip = true;
        
        while (s.curToken().kind != TokenKind.rightBraceToken) {
            if (!leftBraceSkip) skip(s, TokenKind.commaToken);
            else leftBraceSkip = false;
            
            AspStringLiteral asl = AspStringLiteral.parse(s);
            aDictd.stringLits.add(asl);

            skip(s, TokenKind.colonToken);

            AspExpr aeo = AspExpr.parse(s);
            aDictd.exprs.add(aeo);
        }

        skip(s, TokenKind.rightBraceToken);
        leaveParser("dict display");

        return aDictd;
    }

    public int dictSize() {
        return exprs.size();
    }

    public HashMap<RuntimeValue, RuntimeValue> getRuntimeElements() {
        return runtimeElements;
    }

    // Oppsettet inspirert av løsningsforslaget fra ukesoppgave 39, høsten 2021
    @Override
    public void prettyPrint(){
        prettyWrite("{");

        for(int i=0; i< stringLits.size(); i++){
            stringLits.get(i).prettyPrint();
            prettyWrite(": ");
            exprs.get(i).prettyPrint();

            if(i < stringLits.size()-1){
                prettyWrite(", ");
            }
        }
        prettyWrite("}");
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;
        RuntimeValue e = null;

        for (int i = 0; i < stringLits.size(); i++) {
            v = stringLits.get(i).eval(curScope);
            e = exprs.get(i).eval(curScope);

            runtimeElements.put(v, e);
        }

        return new RuntimeDictValue(runtimeElements);
    }
}
