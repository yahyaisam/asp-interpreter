package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.*;
import java.util.ArrayList;

public class RuntimeListValue extends RuntimeValue {

    ArrayList<RuntimeValue> list;

    public RuntimeListValue(ArrayList<RuntimeValue> list) {
        this.list = list;
    }

    @Override
    public String typeName() {
        return "list";
    }

    // Metode som henter listen, brukes i eval-metoden til AspForStmt
    @Override
    public ArrayList<RuntimeValue> getListElems(AspSyntax where) {
        return list;}
    

    @Override
    public String toString() {
        String list_string = "[";
        
        for (int i = 0; i < list.size(); i++) {
            if (i != 0 && i != list.size()) list_string = list_string + (",");
            list_string = list_string + list.get(i);
        }

        list_string = list_string + ("]");
        return list_string;
    }


    @Override
    public String showInfo() {
        String list_string = "[";
        
        for (int i = 0; i < list.size(); i++) {
            RuntimeValue v = list.get(i);
            String s = "";
            if (i != 0 && i != list.size()) list_string = list_string + (", ");

            if (v instanceof RuntimeStringValue) s = v.showInfo();
            else {
                s = v.toString();
            }

            list_string = list_string + s;
        }

        list_string = list_string + ("]");

        return list_string;
    }


    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return this.list.isEmpty();
    }


    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        
        if (v instanceof RuntimeIntValue) {
            
            ArrayList<RuntimeValue> result = new ArrayList<>();

            for (int i = 0; i < v.getIntValue("int", where); i ++) {
                for (RuntimeValue e : list) {
                    result.add(e);
                }
            }
            return new RuntimeListValue(result);
        }
        
        else {
            runtimeError("'*' undefined for "+v.typeName()+"!", where);
            return null;  
        }
    }


    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v.typeName().equals("none"))  {
            return new RuntimeBoolValue(this.getBoolValue("list", where) == v.getBoolValue("none", where));
        }

        else {
            runtimeError("'!=' undefined for "+v.typeName()+"!", where);
            return null;  
        }
    }


    @Override
	public RuntimeValue evalLen(AspSyntax where) {
       return new RuntimeIntValue(this.list.size());
	}


    @Override
	public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
			
        if (v.typeName().equals("none"))  {
            return new RuntimeBoolValue(this.getBoolValue("list", where) != v.getBoolValue("none", where));
        }

        else {
            runtimeError("'!=' undefined for "+v.typeName()+"!", where);
            return null;  

        }
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!this.getBoolValue("list", where));
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        list.set((int) inx.getIntValue("Assignment", where), val);
        }

    @Override
	public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {

		if (v.typeName().equals("int")) {

			return list.get((int) v.getIntValue("int", where));
		}

		else {
			runtimeError("'[...]' undefined for " + v.typeName() + "!", where);
			return null; 
		}
	}
}
