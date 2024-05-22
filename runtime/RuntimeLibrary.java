// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;


import java.util.ArrayList;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
	    //-- Must be changed in part 4:

        assign("float", new RuntimeFunc("float"){
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> params, AspSyntax where){
                checkNumParams(params, 1, "float", where);

                return new RuntimeFloatValue(params.get(0).getFloatValue("float", where));
            }
        });

        assign("input", new RuntimeFunc("input") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> params, AspSyntax where){
                String outString = params.get(0).getStringValue("print", where);
                outString = outString.substring(0, outString.length());
                System.out.print(outString);

                return new RuntimeStringValue("\"" + keyboard.nextLine() + "\"");
            }
        });

        assign("int", new RuntimeFunc("int") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> params, AspSyntax where){
                checkNumParams(params, 1, "int", where);

                return new RuntimeIntValue(params.get(0).getIntValue("int", where));
            }
        });
        
        
        assign("len", new RuntimeFunc("len") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
            
            checkNumParams(actualParams, 1, "len", where);
            return actualParams.get(0).evalLen(where);
        }});

        assign("print", new RuntimeFunc("print") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> parameters, AspSyntax where) {
                System.out.println("");
                for (int i = 0 ; i < parameters.size() ; i ++ ) {

                    String outstring = parameters.get(i).toString();
                    String firstChar = outstring.charAt(0) + "";
                    

                    System.out.print(outstring);
                    System.out.print(" ");
                }
        
                RuntimeNoneValue n = new RuntimeNoneValue();
                 
                return n;
            }
        });

        assign("range", new RuntimeFunc("range") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> parameters, AspSyntax Where) {
                checkNumParams(parameters, 2, "range", Where);
                long beginning = parameters.get(0).getIntValue("range", Where);
                long finish = parameters.get(1).getIntValue("range", Where);
                if (beginning > finish) {
                    runtimeError("range: the first parameter of the built-in function range(-,-) needs to be less than the end", Where);
                }
                else {
                    
                    ArrayList<RuntimeValue> rl = new ArrayList<>();
                    for (long i = beginning ; i < finish ; i ++) {
                        rl.add(new RuntimeIntValue(i));
                    }
                    RuntimeListValue l = new RuntimeListValue(rl);
                    return l;
                }
                return null;
            }
        });

        assign("str", new RuntimeFunc("str") {
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> params, AspSyntax where) {
            checkNumParams(params, 1, "str", where);
            
            return new RuntimeStringValue(params.get(0).getStringValue("str", where));
        }
        });
    }

    private void checkNumParams(ArrayList<RuntimeValue> actArgs, 
				int nCorrect, String id, AspSyntax where) {
	if (actArgs.size() != nCorrect)
	    RuntimeValue.runtimeError("Wrong number of parameters to "+id+"!",where);
    }
}
