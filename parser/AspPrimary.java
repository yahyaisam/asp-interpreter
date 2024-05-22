package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import java.util.ArrayList;

class AspPrimary extends AspSyntax {
    
    AspAtom aa;
    ArrayList<AspPrimarySuffix> aps = new ArrayList<>();    

    AspPrimary(int n) {
        super(n);
    }
    
    static AspPrimary parse(Scanner s) {
        enterParser("primary");
        AspPrimary ap = new AspPrimary(s.curLineNum());
    
        ap.aa = AspAtom.parse(s);
        
        while (s.curToken().kind.equals(TokenKind.leftParToken) 
        || s.curToken().kind.equals(TokenKind.leftBracketToken)) {
            
            ap.aps.add(AspPrimarySuffix.parse(s));
        }
    

        leaveParser("primary");
        return ap;
    }

    @Override
    public void prettyPrint() {
        aa.prettyPrint();
        
        for (int i = 0; i < aps.size(); i++) {
            aps.get(i).prettyPrint();
        }
    }
    
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
    RuntimeValue returnValue = aa.eval(curScope);

    ArrayList<RuntimeValue> args;

    // Med lister og lignende er primary suffix indeksen.
    int i = 0;

    boolean firstCall = true;
    for(AspPrimarySuffix suf : aps){
        
        boolean type = suf.getType();

    // dette fungerer ikke: (suf instanceof AspSubscription)
    // bruker i stedet "type" som er angitt i AspPrimarysuffix sin klasse
    // "type" er en boolean som sier om vi har en instans av Subscription eller Arguments
      if(type){

          returnValue = returnValue.evalSubscription(suf.eval(curScope), this);
          i ++;
          
    // dette fungerer ikke: (suf instanceof AspArguments)
      }else if(!type){
    
        RuntimeListValue rl = (RuntimeListValue) suf.eval(curScope);
        args = rl.getListElems(suf);

        if (firstCall) {

            String s = rl.showInfo();            
            
            trace("Call function: " + returnValue.toString() + " with params: " + s);
            firstCall = false;
        }
        returnValue = returnValue.evalFuncCall(args, this);
      }
    }
    return returnValue;
  }

  
}
