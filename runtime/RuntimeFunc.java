package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.parser.AspSuite;

public class RuntimeFunc extends RuntimeValue {
	RuntimeValue funcName;
    ArrayList<RuntimeValue> funcParamNames;
    AspSuite funcSuite;
    String name;
    RuntimeScope callScope;

    public RuntimeFunc(RuntimeValue funcName, ArrayList<RuntimeValue> funcParamNames, AspSuite funcSuite, RuntimeScope callScope) {
        this.funcName = funcName;
        this.funcParamNames = funcParamNames;
        this.funcSuite = funcSuite;
        name = funcName.toString();
        this.callScope = callScope;
    }

    public RuntimeFunc(String name) {
        this.name = name;
    }
    
    @Override
    public String typeName() {
        return "func";
    }

    public void writeInfo() {
        System.out.println("FuncName: " + funcName + 
        " params to func: " + funcParamNames);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where){
        RuntimeValue returnVal = null;
     
        
        // Vi kontrollerer at vi har riktig antall parametre inn i forhold til argumenter
        // for funksjonskaller
        if (funcParamNames.size() == actualParams.size()) {

            // Initialize the function's scope with the scope that called the current function
            RuntimeScope funcScope = new RuntimeScope(callScope);

            int i = 0;
            
            for (RuntimeValue curVal : actualParams) {
            
                if (curVal != null)funcScope.assign(funcParamNames.get(i).getStringValue("string", where), curVal); 
                
                else funcScope.assign(funcParamNames.get(i).getStringValue("string", where), actualParams.get(i));
                i ++;
            }

          
            // Evaluate the suite with new scope
            try {
                
                return funcSuite.eval(funcScope);}

            catch (RuntimeReturnValue e){
     
                return e.value;
            }
        }

        else Main.error("params and args not matching");

        return null;

    }
}
