package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.*;

public class RuntimeStringValue extends RuntimeValue {
	public String stringValue;
    boolean ifSpecial = false;
    public boolean firstCharNow = false;
    String replaced = "";

    public RuntimeStringValue(String s) {

        stringValue = s;
        String fc = stringValue.charAt(0) + "";
        
        // we store strings without their respective string chars
        if (fc.equals("\"") || fc.equals("'")) stringValue = stringValue.substring(1, stringValue.length()-1);
    }

    @Override
    public String typeName() {
        return "string";
    }

    @Override
    public String showInfo() {
        return "'" + stringValue + "'";

    }

    @Override 
    public String toString() {
        return stringValue;
        // String s2 = this.stringValue.replace("\"", "");
        // if (ifSpecial) return "\"" + stringValue + "\"";
        // else return stringValue;
    }
    
    @Override
    public String getUnModStringValue() {
        return stringValue;
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
       
        try {
            String pot_num = this.stringValue.replace("\"", "");
            Long num = Long.parseLong(pot_num);
            return num;
        }
        
        catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
		return (this.stringValue.length() != 2);
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return stringValue;
    }

    @Override
	public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
            String s1 = this.getStringValue("evalAdd", where);
           
            if (v.typeName().equals("string")) {
                String s2 = v.getStringValue("string", where);
                String combString = "\"" + s1 + s2 + "\"";
                return new RuntimeStringValue(combString);
            }

            else {
                runtimeError("'+' undefined for "+v.typeName()+"!", where);
                    return null;
                }
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {

        // subtract 2 due to "" or '' is evaluated as part of the length of the string
        String s2 = this.getStringValue("string", where).replace("\"", "");
        return new RuntimeIntValue((long) s2.length());
        }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {

        String combString = "";
        String build = this.stringValue.replace("\"", "");

        if (v.typeName().equals("int")) {

            for (int i = 0 ; i < v.getIntValue("int", where) ; i ++) {
                combString = combString + build;
            }

            combString = "\"" + combString + "\"";
            
            return new RuntimeStringValue(combString);
        }

        else {
            runtimeError("'*' undefined for "+v.typeName()+"!", where);
            return null;  // Required by the compiler!

            }
        }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        
        if (v.typeName().equals("string"))
            return new RuntimeBoolValue(this.stringValue.equals(v.getStringValue("string", where)));

        else {
            runtimeError("'==' undefined for "+v.typeName()+"!", where);
            return null;  // Required by the compiler!
            }
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        
        if (v.typeName().equals("string"))
            return new RuntimeBoolValue(!this.stringValue.equals(v.getStringValue("string", where)));

        else {
            runtimeError("'!=' undefined for "+v.typeName()+"!", where);
            return null;  // Required by the compiler!
            }
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!this.getBoolValue("string", where));
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {

        if (v.typeName().equals("string"))
            return new RuntimeBoolValue(this.stringValue.length() <= v.getStringValue("string", where).length());

        else {
            runtimeError("'<=' undefined for "+v.typeName()+"!", where);
            return null;  // Required by the compiler!
            }
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {

        if (v.typeName().equals("string"))
            return new RuntimeBoolValue(this.stringValue.length() < v.getStringValue("string", where).length());

        else {
            runtimeError("'<' undefined for "+v.typeName()+"!", where);
            return null;  // Required by the compiler!
            }
    }
    
    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {

        if (v.typeName().equals("string"))
            return new RuntimeBoolValue(this.stringValue.length() >= v.getStringValue("string", where).length());

        else {
            runtimeError("'>=' undefined for "+v.typeName()+"!", where);
            return null;  // Required by the compiler!
            }
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {

        if (v.typeName().equals("string"))
            return new RuntimeBoolValue(this.stringValue.length() > v.getStringValue("string", where).length());

        else {
            runtimeError("'>' undefined for "+v.typeName()+"!", where);
            return null;  // Required by the compiler!
            }
    }

    @Override
	public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
		if (v.typeName().equals("int")) {
           
            String s = this.getStringValue("string", where).replace("\"", "");
            s = s.replace("\'", "");
			return new RuntimeStringValue("\'" + Character.toString(s.charAt((int) v.getIntValue("int", where))) + "\'");
		}

		else {
			runtimeError("'[...]' undefined for " + v.typeName() + "!", where);
			return null; 
		}
	}
}
